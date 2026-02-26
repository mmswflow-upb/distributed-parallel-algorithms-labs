#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>

static void die_if(int cond, const char* msg, int code) __attribute__((unused));
static void die_if(int cond, const char* msg, int code) {
    if (cond) { fprintf(stderr, "%s\n", msg); MPI_Abort(MPI_COMM_WORLD, code); }
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int world_size = 0, rank = 0;
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    if (world_size != 2) {
        if (rank == 0) {
            fprintf(stderr,
                "Exercise 1 requires exactly 2 processes. Run: mpirun -np 2 ./bin/ex1_sum_array\n");
        }
        MPI_Finalize();
        return 1; // or MPI_Abort(MPI_COMM_WORLD, 1);
    }

    const int n = 6;
    const int a[6] = {1, 2, 3, 4, 5, 6};

    // Split work evenly: 3 elements per process.
    const int chunk = n / 2; // = 3
    int start = rank * chunk;
    int end = start + chunk;

    int local_sum = 0;
    for (int i = start; i < end; i++) {
        local_sum += a[i];
    }

    if (rank == 1) {
        MPI_Send(&local_sum, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
    } else { // rank 0
        int recv_sum = 0;
        MPI_Recv(&recv_sum, 1, MPI_INT, 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        int total = local_sum + recv_sum;

        printf("[ex1] Array: ");
        for (int i = 0; i < n; i++) printf("%d%s", a[i], (i == n-1 ? "" : " "));
        printf("\n[ex1] Partial sums: rank0=%d, rank1=%d\n", local_sum, recv_sum);
        printf("[ex1] Total sum = %d\n", total);
    }

    MPI_Finalize();
    return 0;
}
