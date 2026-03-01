#!/usr/bin/env python
import sys

current_key = None
sum_len = 0
count = 0

for line in sys.stdin:
    line = line.strip()
    if not line:
        continue

    parts = line.split("\t")
    if len(parts) != 2:
        continue

    key = parts[0]
    try:
        val = int(parts[1])
    except:
        continue

    if current_key is None:
        current_key = key

    if key != current_key:
        avg = float(sum_len) / float(count) if count else 0.0
        sys.stdout.write("%s\t%f\n" % (current_key, avg))

        current_key = key
        sum_len = val
        count = 1
    else:
        sum_len += val
        count += 1

if current_key is not None:
    avg = float(sum_len) / float(count) if count else 0.0
    sys.stdout.write("%s\t%f\n" % (current_key, avg))
