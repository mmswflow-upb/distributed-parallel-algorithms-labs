#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int rank = -1, size = -1;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    // Exercise 2 uses 2 processes: one computes, the other receives.
    if (size != 2) {
        if (rank == 0) {
            fprintf(stderr, "Exercise 2 requires exactly 2 processes. Run: mpirun -np 2 ./bin/ex2_avg_send\n");
        }
        MPI_Finalize();
        return 1;
    }

    const int n = 10;
    const int a[10] = {3, 7, 2, 9, 4, 5, 1, 8, 6, 10};

    if (rank == 0) {
        int sum = 0;
        for (int i = 0; i < n; i++) sum += a[i];
        double avg = (double)sum / (double)n;

        MPI_Send(&avg, 1, MPI_DOUBLE, 1, 0, MPI_COMM_WORLD);

        printf("[ex2] Rank0 computed average of 10 elements.\n");
        printf("[ex2] Sum=%d, Average=%.6f (sent to rank1)\n", sum, avg);
    } else { // rank 1
        double avg = 0.0;
        MPI_Recv(&avg, 1, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        printf("[ex2] Rank1 received average=%.6f from rank0\n", avg);
    }

    MPI_Finalize();
    return 0;
}
