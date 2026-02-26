#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>

// HW2: Matrix multiplication (parallelized by rows across 2 processes)

static void print_matrix(const char* title, const int* M, int rows, int cols) {
    printf("%s\n", title);
    for (int i = 0; i < rows; i++) {
        printf("  ");
        for (int j = 0; j < cols; j++) {
            printf("%6d", M[i*cols + j]);
        }
        printf("\n");
    }
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int rank=-1, size=-1;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (size != 2) {
        if (rank == 0) {
            fprintf(stderr, "HW2 implementation uses exactly 2 processes. Run: mpirun -np 2 ./bin/hw2_matrix_mul\n");
        }
        MPI_Finalize();
        return 1;
    }

    // Multiply A (4x3) * B (3x4) = C (4x4)
    const int AR=4, AC=3, BR=3, BC=4;
    int A[12] = {
        1, 2, 3,
        4, 5, 6,
        7, 8, 9,
        2, 1, 0
    };
    int B[12] = {
        1,  0, 2, 1,
        0,  1, 3, 2,
        4,  1, 0, 1
    };

    // Broadcast A and B to both ranks (simple for small fixed sizes)
    MPI_Bcast(A, AR*AC, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Bcast(B, BR*BC, MPI_INT, 0, MPI_COMM_WORLD);

    int rows_per_rank = AR / 2; // 2 rows each
    int start_row = rank * rows_per_rank;
    int end_row = start_row + rows_per_rank;

    int localC[8]; // rows_per_rank * BC = 2*4=8
    for (int i = start_row; i < end_row; i++) {
        for (int j = 0; j < BC; j++) {
            int sum = 0;
            for (int k = 0; k < AC; k++) {
                sum += A[i*AC + k] * B[k*BC + j];
            }
            localC[(i - start_row)*BC + j] = sum;
        }
    }

    int C[16]; // 4x4
    MPI_Gather(localC, rows_per_rank*BC, MPI_INT, C, rows_per_rank*BC, MPI_INT, 0, MPI_COMM_WORLD);

    if (rank == 0) {
        print_matrix("[hw2] Matrix A (4x3):", A, AR, AC);
        print_matrix("[hw2] Matrix B (3x4):", B, BR, BC);
        print_matrix("[hw2] C = A*B (4x4), computed in parallel:", C, AR, BC);
    }

    MPI_Finalize();
    return 0;
}
