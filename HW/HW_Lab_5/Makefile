CC=mpicc
CFLAGS=-O2 -Wall -Wextra -std=c11

BIN_DIR=bin
SRC_DIR=src

TARGETS=$(BIN_DIR)/hw1_sum_time_third $(BIN_DIR)/hw2_matrix_mul

all: $(TARGETS)

$(BIN_DIR):
	mkdir -p $(BIN_DIR)

$(BIN_DIR)/hw1_sum_time_third: $(SRC_DIR)/hw1_sum_time_third.c | $(BIN_DIR)
	$(CC) $(CFLAGS) $< -o $@

$(BIN_DIR)/hw2_matrix_mul: $(SRC_DIR)/hw2_matrix_mul.c | $(BIN_DIR)
	$(CC) $(CFLAGS) $< -o $@

clean:
	rm -rf $(BIN_DIR)

.PHONY: all clean
