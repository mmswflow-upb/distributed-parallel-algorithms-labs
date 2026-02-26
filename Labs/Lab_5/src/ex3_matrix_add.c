#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>

static void print_matrix(const char* title, const int* M, int rows, int cols) {
    printf("%s\n", title);
    for (int i = 0; i < rows; i++) {
        printf("  ");
        for (int j = 0; j < cols; j++) {
            printf("%4d", M[i*cols + j]);
        }
        printf("\n");
    }
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int rank = -1, size = -1;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    // Exercise requires splitting between two processes.
    if (size != 2) {
        if (rank == 0) {
            fprintf(stderr, "Exercise 3 requires exactly 2 processes. Run: mpirun -np 2 ./bin/ex3_matrix_add\n");
        }
        MPI_Finalize();
        return 1;
    }

    // Example: 4x4 matrices, split by rows (2 rows per process).
    const int R = 4, C = 4;
    int A[16] = {
        1,  2,  3,  4,
        5,  6,  7,  8,
        9, 10, 11, 12,
        13,14, 15, 16
    };
    int B[16] = {
        16,15,14,13,
        12,11,10, 9,
         8, 7, 6, 5,
         4, 3, 2, 1
    };

    int rows_per_rank = R / 2; // 2
    int start_row = rank * rows_per_rank;
    int end_row = start_row + rows_per_rank;

    int localC[8]; // 2*4
    for (int i = start_row; i < end_row; i++) {
        for (int j = 0; j < C; j++) {
            localC[(i - start_row)*C + j] = A[i*C + j] + B[i*C + j];
        }
    }

    int Cfull[16];
    MPI_Gather(localC, rows_per_rank*C, MPI_INT, Cfull, rows_per_rank*C, MPI_INT, 0, MPI_COMM_WORLD);

    if (rank == 0) {
        print_matrix("[ex3] Matrix A:", A, R, C);
        print_matrix("[ex3] Matrix B:", B, R, C);
        print_matrix("[ex3] A + B (computed in parallel, gathered on rank0):", Cfull, R, C);
    }

    MPI_Finalize();
    return 0;
}
