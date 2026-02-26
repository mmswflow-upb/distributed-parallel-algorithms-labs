#pragma once
#include <cstdio>
#include <cstdlib>
#include <cmath>
#include <algorithm>

#include <cuda_runtime.h>
#include <cublas_v2.h>
#include <cusolverDn.h>

#define CUDA_CHECK(call) do {                                  \
    cudaError_t err = (call);                                  \
    if (err != cudaSuccess) {                                  \
        fprintf(stderr, "CUDA error %s:%d: %s\n",              \
                __FILE__, __LINE__, cudaGetErrorString(err));  \
        std::exit(1);                                          \
    }                                                          \
} while(0)

#define CUBLAS_CHECK(call) do {                                \
    cublasStatus_t st = (call);                                \
    if (st != CUBLAS_STATUS_SUCCESS) {                         \
        fprintf(stderr, "cuBLAS error %s:%d: %d\n",            \
                __FILE__, __LINE__, (int)st);                  \
        std::exit(1);                                          \
    }                                                          \
} while(0)

#define CUSOLVER_CHECK(call) do {                              \
    cusolverStatus_t st = (call);                              \
    if (st != CUSOLVER_STATUS_SUCCESS) {                       \
        fprintf(stderr, "cuSOLVER error %s:%d: %d\n",          \
                __FILE__, __LINE__, (int)st);                  \
        std::exit(1);                                          \
    }                                                          \
} while(0)

inline void cpu_matmul(const float* A, const float* B, float* C, int M, int K, int N) {
    for (int i = 0; i < M; i++) {
        for (int j = 0; j < N; j++) {
            float sum = 0;
            for (int k = 0; k < K; k++) sum += A[i*K + k] * B[k*N + j];
            C[i*N + j] = sum;
        }
    }
}

inline double max_abs_diff(const float* a, const float* b, int n) {
    double m = 0;
    for (int i = 0; i < n; i++) {
        double d = std::fabs((double)a[i] - (double)b[i]);
        if (d > m) m = d;
    }
    return m;
}

inline void cpu_identity_check(const double* Arow, const double* Ainvrow, int N) {
    double maxErr = 0.0;
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            double sum = 0;
            for (int k = 0; k < N; k++)
                sum += Arow[i*N + k] * Ainvrow[k*N + j];
            double target = (i == j) ? 1.0 : 0.0;
            maxErr = std::max(maxErr, std::fabs(sum - target));
        }
    }
    printf("Max |A*Ainv - I| = %.3e\n", maxErr);
}
