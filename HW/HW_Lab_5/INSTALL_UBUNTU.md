# Ubuntu setup (OpenMPI + build tools)

The lab slides mention installing MPI on Ubuntu using either **MPICH** (`apt-get install mpich`) or building OpenMPI from source.
**Use OpenMPI** — MPICH has known PMI issues on Ubuntu desktop that cause processes to not see each other (`world_size=1`), making all multi-process programs fail silently.

## 1) Install prerequisites

```bash
sudo apt-get update
sudo apt-get install -y build-essential openmpi-bin libopenmpi-dev
```

If you already have MPICH installed, remove it first:

```bash
sudo apt-get remove --purge mpich && sudo apt-get install openmpi-bin libopenmpi-dev
```

Verify:

```bash
mpicc --version
mpirun --version
```

## 2) Build the lab and homework

From the repo root:

```bash
cd DS_Lab5_Mohamad-Mario_SAKKA

# Build lab
cd LAB
make

# Build homework
cd ../HW
make
```

## 3) Run programs

Use the `-np` flag to set the number of MPI processes (as shown in the slides).

Example:

```bash
cd LAB
mpirun -np 2 ./bin/ex1_sum_array
```

If you see an error about running as root, either run as a normal user or (not recommended) add:

```bash
mpirun --allow-run-as-root -np 2 ./bin/ex1_sum_array
```
