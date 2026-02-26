#pragma once
#include "utils.cuh"

void gpu_matmul(const float* hA, const float* hB, float* hC, int M, int K, int N);
