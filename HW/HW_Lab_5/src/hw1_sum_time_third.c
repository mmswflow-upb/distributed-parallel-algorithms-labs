#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>

// HW1: Split array into 2 arrays; compute sums in parallel processes; return final sum and processing time on a 3rd process.

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int rank=-1, size=-1;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (size != 3) {
        if (rank == 0) {
            fprintf(stderr, "HW1 requires exactly 3 processes. Run: mpirun -np 3 ./bin/hw1_sum_time_third\n");
        }
        MPI_Finalize();
        return 1;
    }

    // "array above" (from Exercise 1): 6 elements
    const int n = 6;
    int a[6] = {1, 2, 3, 4, 5, 6};

    // Split into two arrays of 3 elements each
    int part_size = n / 2; // 3
    int part0[3], part1[3];
    for (int i = 0; i < part_size; i++) {
        part0[i] = a[i];
        part1[i] = a[i + part_size];
    }

    // Synchronize before timing.
    MPI_Barrier(MPI_COMM_WORLD);
    double t0 = MPI_Wtime();

    if (rank == 0 || rank == 1) {
        int local_sum = 0;
        if (rank == 0) {
            for (int i = 0; i < part_size; i++) local_sum += part0[i];
        } else {
            for (int i = 0; i < part_size; i++) local_sum += part1[i];
        }
        // Send partial sum to process 2
        MPI_Send(&local_sum, 1, MPI_INT, 2, 0, MPI_COMM_WORLD);
    } else { // rank 2 (collector)
        int s0 = 0, s1 = 0;
        MPI_Recv(&s0, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(&s1, 1, MPI_INT, 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        double t1 = MPI_Wtime();
        int total = s0 + s1;

        printf("[hw1] Original array: ");
        for (int i = 0; i < n; i++) printf("%d%s", a[i], (i==n-1?"":" "));
        printf("\n[hw1] Split arrays:\n");
        printf("      part0 (rank0): %d %d %d\n", part0[0], part0[1], part0[2]);
        printf("      part1 (rank1): %d %d %d\n", part1[0], part1[1], part1[2]);

        printf("[hw1] Partial sums: rank0=%d, rank1=%d\n", s0, s1);
        printf("[hw1] Final sum (computed on rank2) = %d\n", total);
        printf("[hw1] Processing time (barrier->final recv) = %.9f seconds\n", (t1 - t0));
    }

    MPI_Finalize();
    return 0;
}
