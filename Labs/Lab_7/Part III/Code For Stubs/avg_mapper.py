#!/usr/bin/env python
import sys

for line in sys.stdin:
    for word in line.strip().split():
        if not word:
            continue
        first = word[0]
        length = len(word)
        sys.stdout.write("%s\t%d\n" % (first, length))