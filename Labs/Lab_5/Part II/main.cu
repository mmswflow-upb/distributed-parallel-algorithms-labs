#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <chrono>

#include <cuda_runtime.h>

#include "matmul.cuh"
#include "inverse.cuh"
#include "utils.cuh"

static void print_device_info() {
    int dev = 0;
    CUDA_CHECK(cudaGetDevice(&dev));

    cudaDeviceProp prop{};
    CUDA_CHECK(cudaGetDeviceProperties(&prop, dev));

    int runtimeVersion = 0, driverVersion = 0;
    CUDA_CHECK(cudaRuntimeGetVersion(&runtimeVersion));
    CUDA_CHECK(cudaDriverGetVersion(&driverVersion));

    printf("==== CUDA / GPU INFO ====\n");
    printf("Device ID: %d\n", dev);
    printf("Name     : %s\n", prop.name);
    printf("CC       : %d.%d\n", prop.major, prop.minor);
    printf("GlobalMem: %.2f GB\n", prop.totalGlobalMem / (1024.0 * 1024.0 * 1024.0));
    printf("SMs      : %d\n", prop.multiProcessorCount);
    printf("WarpSize : %d\n", prop.warpSize);
    printf("MaxThreadsPerBlock: %d\n", prop.maxThreadsPerBlock);
    printf("CUDA Driver Version : %d\n", driverVersion);
    printf("CUDA Runtime Version: %d\n", runtimeVersion);
    printf("=========================\n\n");
}

template <typename T>
static void print_top_left(const char* name, const T* M, int rows, int cols, int ld_cols, int r=4, int c=4) {
    // M is row-major, ld_cols = number of columns
    int R = (rows < r) ? rows : r;
    int C = (cols < c) ? cols : c;

    printf("%s (top-left %dx%d):\n", name, R, C);
    for (int i = 0; i < R; i++) {
        for (int j = 0; j < C; j++) {
            if constexpr (std::is_same<T, float>::value) {
                printf("%10.5f ", (double)M[i*ld_cols + j]);
            } else {
                printf("%10.6lf ", (double)M[i*ld_cols + j]);
            }
        }
        printf("\n");
    }
    printf("\n");
}

static double l2_norm_float(const float* x, int n) {
    double s = 0.0;
    for (int i = 0; i < n; i++) s += (double)x[i] * (double)x[i];
    return std::sqrt(s);
}

static double l2_norm_double(const double* x, int n) {
    double s = 0.0;
    for (int i = 0; i < n; i++) s += x[i] * x[i];
    return std::sqrt(s);
}

static double checksum_float(const float* x, int n) {
    double s = 0.0;
    for (int i = 0; i < n; i++) s += (double)x[i];
    return s;
}

static double checksum_double(const double* x, int n) {
    double s = 0.0;
    for (int i = 0; i < n; i++) s += x[i];
    return s;
}

int main(int argc, char** argv) {
    print_device_info();

    // Allow passing sizes: matrix_ops.exe M K N
    int M = 64, K = 64, N = 64;
    if (argc == 4) {
        M = std::atoi(argv[1]);
        K = std::atoi(argv[2]);
        N = std::atoi(argv[3]);
    }

    printf("==== TASK (a) MATRIX MULTIPLICATION ====\n");
    printf("Computing C = A(%dx%d) * B(%dx%d)\n\n", M, K, K, N);

    float* A    = (float*)malloc((size_t)M*K*sizeof(float));
    float* B    = (float*)malloc((size_t)K*N*sizeof(float));
    float* Cgpu = (float*)malloc((size_t)M*N*sizeof(float));
    float* Ccpu = (float*)malloc((size_t)M*N*sizeof(float));

    for (int i = 0; i < M*K; i++) A[i] = (float)((i % 17) - 8) / 7.0f;
    for (int i = 0; i < K*N; i++) B[i] = (float)((i % 13) - 6) / 5.0f;

    print_top_left("A", A, M, K, K);
    print_top_left("B", B, K, N, N);

    // Time GPU matmul using CUDA events
    cudaEvent_t start, stop;
    CUDA_CHECK(cudaEventCreate(&start));
    CUDA_CHECK(cudaEventCreate(&stop));

    CUDA_CHECK(cudaEventRecord(start));
    gpu_matmul(A, B, Cgpu, M, K, N);
    CUDA_CHECK(cudaEventRecord(stop));
    CUDA_CHECK(cudaEventSynchronize(stop));

    float msGpu = 0.0f;
    CUDA_CHECK(cudaEventElapsedTime(&msGpu, start, stop));

    // Time CPU matmul
    auto t0 = std::chrono::high_resolution_clock::now();
    cpu_matmul(A, B, Ccpu, M, K, N);
    auto t1 = std::chrono::high_resolution_clock::now();
    double msCpu = std::chrono::duration<double, std::milli>(t1 - t0).count();

    double maxDiff = max_abs_diff(Cgpu, Ccpu, M*N);
    double normC   = l2_norm_float(Cgpu, M*N);
    double sumC    = checksum_float(Cgpu, M*N);

    print_top_left("C (GPU)", Cgpu, M, N, N);
    printf("[MatMul] GPU time: %.3f ms\n", msGpu);
    printf("[MatMul] CPU time: %.3f ms\n", msCpu);
    printf("[MatMul] ||C||_2 (GPU): %.6e\n", normC);
    printf("[MatMul] checksum(C) (GPU): %.6e\n", sumC);
    printf("[MatMul] Max abs diff GPU vs CPU: %.6e\n", maxDiff);
    printf("=======================================\n\n");

    CUDA_CHECK(cudaEventDestroy(start));
    CUDA_CHECK(cudaEventDestroy(stop));

    // ---- TASK (b): Inverse ----
    printf("==== TASK (b) MATRIX INVERSE ====\n");
    int NI = 8;
    if (argc == 5) {
        NI = std::atoi(argv[4]);
    }
    printf("Computing inverse of Ainv for A(%dx%d)\n\n", NI, NI);

    double* Ai   = (double*)malloc((size_t)NI*NI*sizeof(double));
    double* Ainv = (double*)malloc((size_t)NI*NI*sizeof(double));

    // diagonal dominant so it's invertible and well-conditioned for demo
    for (int i = 0; i < NI; i++) {
        for (int j = 0; j < NI; j++) {
            double v = (i == j) ? 4.0 : 0.1;
            Ai[i*NI + j] = v + 0.01 * (i + j);
        }
    }

    print_top_left("A (inverse input)", Ai, NI, NI, NI);

    // GPU timing for inverse (roughly the total time of cusolver calls)
    cudaEvent_t s2, e2;
    CUDA_CHECK(cudaEventCreate(&s2));
    CUDA_CHECK(cudaEventCreate(&e2));

    CUDA_CHECK(cudaEventRecord(s2));
    gpu_inverse_cusolver(Ai, Ainv, NI);
    CUDA_CHECK(cudaEventRecord(e2));
    CUDA_CHECK(cudaEventSynchronize(e2));

    float msInv = 0.0f;
    CUDA_CHECK(cudaEventElapsedTime(&msInv, s2, e2));

    double sumInv = checksum_double(Ainv, NI*NI);
    double normInv = l2_norm_double(Ainv, NI*NI);

    print_top_left("Ainv (GPU)", Ainv, NI, NI, NI);
    printf("[Inverse] GPU time (cuSOLVER): %.3f ms\n", msInv);
    printf("[Inverse] ||Ainv||_2: %.6e\n", normInv);
    printf("[Inverse] checksum(Ainv): %.6e\n", sumInv);

    // existing verification prints max |A*Ainv - I|
    cpu_identity_check(Ai, Ainv, NI);

    printf("================================\n");

    CUDA_CHECK(cudaEventDestroy(s2));
    CUDA_CHECK(cudaEventDestroy(e2));

    free(A); free(B); free(Cgpu); free(Ccpu);
    free(Ai); free(Ainv);
    return 0;
}