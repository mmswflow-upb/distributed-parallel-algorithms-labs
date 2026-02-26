# HW — Homework (MPI, C)

Homework tasks are from page **15** of the lab slides.

## Build

```bash
make
```

Outputs are placed in `bin/`.

## Run

### HW1 — Split the array into 2 arrays, compute sums in parallel, return final sum and processing time on a 3rd process

Requires **3 processes**:

```bash
mpirun -np 3 ./bin/hw1_sum_time_third
```

### HW2 — Matrix multiplication

This implementation parallelizes by rows and works with **2 processes**:

```bash
mpirun -np 2 ./bin/hw2_matrix_mul
```
