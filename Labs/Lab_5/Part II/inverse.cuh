#pragma once
#include "utils.cuh"

void gpu_inverse_cusolver(const double* hA_rowmajor, double* hAinv_rowmajor, int N);
