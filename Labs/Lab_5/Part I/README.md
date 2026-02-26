# LAB — Exercises (MPI, C)

Exercises are from page **14** of the lab slides.

## Build

```bash
make
```

Outputs are placed in `bin/`.

## Run

### Exercise 1 — Parallel sum of an array (6 elements) using 2 processes

```bash
mpirun -np 2 ./bin/ex1_sum_array
```

### Exercise 2 — Compute average in one process and send it to a second process

```bash
mpirun -np 2 ./bin/ex2_avg_send
```

### Exercise 3 — Parallel addition of two matrices using 2 processes

```bash
mpirun -np 2 ./bin/ex3_matrix_add
```
