#include "inverse.cuh"
#include <vector>

static void make_identity(double* I, int N) {
    for (int j = 0; j < N; j++)
        for (int i = 0; i < N; i++)
            I[j*N + i] = (i == j) ? 1.0 : 0.0; // column-major
}

static void host_rowmajor_to_colmajor(const double* Arow, double* Acol, int N) {
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            Acol[j*N + i] = Arow[i*N + j];
}

static void host_colmajor_to_rowmajor(const double* Acol, double* Arow, int N) {
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            Arow[i*N + j] = Acol[j*N + i];
}

void gpu_inverse_cusolver(const double* hA_rowmajor, double* hAinv_rowmajor, int N) {
    cusolverDnHandle_t solver = nullptr;
    CUSOLVER_CHECK(cusolverDnCreate(&solver));

    std::vector<double> hA_col((size_t)N*N);
    std::vector<double> hInv_col((size_t)N*N);
    host_rowmajor_to_colmajor(hA_rowmajor, hA_col.data(), N);
    make_identity(hInv_col.data(), N);

    double* dA = nullptr;
    double* dB = nullptr;
    int* dIpiv = nullptr;
    int* dInfo = nullptr;

    CUDA_CHECK(cudaMalloc(&dA, (size_t)N * N * sizeof(double)));
    CUDA_CHECK(cudaMalloc(&dB, (size_t)N * N * sizeof(double)));
    CUDA_CHECK(cudaMalloc(&dIpiv, (size_t)N * sizeof(int)));
    CUDA_CHECK(cudaMalloc(&dInfo, sizeof(int)));

    CUDA_CHECK(cudaMemcpy(dA, hA_col.data(), (size_t)N * N * sizeof(double), cudaMemcpyHostToDevice));
    CUDA_CHECK(cudaMemcpy(dB, hInv_col.data(), (size_t)N * N * sizeof(double), cudaMemcpyHostToDevice));

    int workSize = 0;
    CUSOLVER_CHECK(cusolverDnDgetrf_bufferSize(solver, N, N, dA, N, &workSize));
    double* dWork = nullptr;
    CUDA_CHECK(cudaMalloc(&dWork, (size_t)workSize * sizeof(double)));

    CUSOLVER_CHECK(cusolverDnDgetrf(solver, N, N, dA, N, dWork, dIpiv, dInfo));
    CUDA_CHECK(cudaDeviceSynchronize());

    int hInfo = 0;
    CUDA_CHECK(cudaMemcpy(&hInfo, dInfo, sizeof(int), cudaMemcpyDeviceToHost));
    if (hInfo != 0) {
        fprintf(stderr, "Matrix is singular or factorization failed (info=%d)\n", hInfo);
        std::exit(1);
    }

    CUSOLVER_CHECK(cusolverDnDgetrs(solver, CUBLAS_OP_N, N, N, dA, N, dIpiv, dB, N, dInfo));
    CUDA_CHECK(cudaDeviceSynchronize());

    CUDA_CHECK(cudaMemcpy(&hInfo, dInfo, sizeof(int), cudaMemcpyDeviceToHost));
    if (hInfo != 0) {
        fprintf(stderr, "Solve failed (info=%d)\n", hInfo);
        std::exit(1);
    }

    CUDA_CHECK(cudaMemcpy(hInv_col.data(), dB, (size_t)N * N * sizeof(double), cudaMemcpyDeviceToHost));
    host_colmajor_to_rowmajor(hInv_col.data(), hAinv_rowmajor, N);

    cudaFree(dWork);
    cudaFree(dA);
    cudaFree(dB);
    cudaFree(dIpiv);
    cudaFree(dInfo);

    cusolverDnDestroy(solver);
}
