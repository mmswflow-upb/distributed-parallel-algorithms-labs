#include <cstdio>
#include <cstdlib>

#include "matmul.cuh"
#include "inverse.cuh"
#include "utils.cuh"

int main() {
    // ---------------- a) Matrix multiplication demo ----------------
    int M = 64, K = 64, N = 64;
    float* A = (float*)malloc((size_t)M*K*sizeof(float));
    float* B = (float*)malloc((size_t)K*N*sizeof(float));
    float* Cgpu = (float*)malloc((size_t)M*N*sizeof(float));
    float* Ccpu = (float*)malloc((size_t)M*N*sizeof(float));

    for (int i = 0; i < M*K; i++) A[i] = (float)((i % 17) - 8) / 7.0f;
    for (int i = 0; i < K*N; i++) B[i] = (float)((i % 13) - 6) / 5.0f;

    gpu_matmul(A, B, Cgpu, M, K, N);
    cpu_matmul(A, B, Ccpu, M, K, N);

    printf("[MatMul] Max abs diff GPU vs CPU: %.3e\n",
           max_abs_diff(Cgpu, Ccpu, M*N));

    // ---------------- b) Matrix inverse demo ----------------
    int NI = 8;
    double* Ai = (double*)malloc((size_t)NI*NI*sizeof(double));
    double* Ainv = (double*)malloc((size_t)NI*NI*sizeof(double));

    for (int i = 0; i < NI; i++) {
        for (int j = 0; j < NI; j++) {
            double v = (i == j) ? 4.0 : 0.1;
            Ai[i*NI + j] = v + 0.01 * (i + j);
        }
    }

    gpu_inverse_cusolver(Ai, Ainv, NI);
    cpu_identity_check(Ai, Ainv, NI);

    free(A); free(B); free(Cgpu); free(Ccpu);
    free(Ai); free(Ainv);
    return 0;
}
