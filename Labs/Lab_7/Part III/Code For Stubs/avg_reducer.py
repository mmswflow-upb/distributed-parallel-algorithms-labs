#!/usr/bin/env python3
import sys

current_key = None
sum_len = 0
count = 0

for line in sys.stdin:
    line = line.strip()
    if not line:
        continue

    # Expect "key\tvalue"
    parts = line.split("\t")
    if len(parts) != 2:
        continue

    key, val_str = parts[0], parts[1]
    try:
        val = int(val_str)
    except ValueError:
        continue

    if current_key is None:
        current_key = key

    if key != current_key:
        # output average for previous key
        avg = (sum_len / count) if count else 0.0
        sys.stdout.write(f"{current_key}\t{avg}\n")

        # reset for new key
        current_key = key
        sum_len = val
        count = 1
    else:
        sum_len += val
        count += 1

# flush last key
if current_key is not None:
    avg = (sum_len / count) if count else 0.0
    sys.stdout.write(f"{current_key}\t{avg}\n")