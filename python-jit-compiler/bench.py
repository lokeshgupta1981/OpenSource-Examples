"""Runs recipe_jit_bench.py 5 times and reports the median elapsed time.

Usage:
    PYTHON_JIT=0 python3 bench.py
    PYTHON_JIT=1 python3 bench.py

This does not toggle PYTHON_JIT itself; run it once with the variable set to
0 and once set to 1, on a JIT-enabled interpreter, and compare the two
printed medians.
"""
import statistics
import subprocess
import sys

RUNS = 5


def main():
    times = []
    for i in range(1, RUNS + 1):
        out = subprocess.run(
            [sys.executable, "recipe_jit_bench.py"],
            capture_output=True, text=True, check=True,
        ).stdout
        for line in out.splitlines():
            if line.startswith("elapsed_seconds:"):
                t = float(line.split(":", 1)[1])
                times.append(t)
                print(f"run {i}: {t:.4f}s")
        if i == 1:
            print(out.strip().split("\n")[0:2])

    times.sort()
    print(f"median: {statistics.median(times):.4f}s")
    print(f"min: {min(times):.4f}s  max: {max(times):.4f}s")


if __name__ == "__main__":
    main()
