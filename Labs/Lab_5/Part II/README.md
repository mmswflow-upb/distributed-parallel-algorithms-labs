# MATRIX MULTIPLICATION AND INVERSE IN CUDA

Implements:
- (a) Matrix multiplication using a tiled CUDA kernel (`matmul.cu`)
- (b) Matrix inverse using cuSOLVER (LU factorization + solve) (`inverse.cu`)

## Build
```bash
nvcc -O2 main.cu matmul.cu inverse.cu -o matrix_ops -lcublas -lcusolver
```

## Run
```bash
./matrix_ops
```
