CC=mpicc
CFLAGS=-O2 -Wall -Wextra -std=c11

BIN_DIR=bin
SRC_DIR=src

TARGETS=$(BIN_DIR)/ex1_sum_array $(BIN_DIR)/ex2_avg_send $(BIN_DIR)/ex3_matrix_add

all: $(TARGETS)

$(BIN_DIR):
	mkdir -p $(BIN_DIR)

$(BIN_DIR)/ex1_sum_array: $(SRC_DIR)/ex1_sum_array.c | $(BIN_DIR)
	$(CC) $(CFLAGS) $< -o $@

$(BIN_DIR)/ex2_avg_send: $(SRC_DIR)/ex2_avg_send.c | $(BIN_DIR)
	$(CC) $(CFLAGS) $< -o $@

$(BIN_DIR)/ex3_matrix_add: $(SRC_DIR)/ex3_matrix_add.c | $(BIN_DIR)
	$(CC) $(CFLAGS) $< -o $@

clean:
	rm -rf $(BIN_DIR)

.PHONY: all clean
