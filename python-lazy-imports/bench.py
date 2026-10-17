"""Times shelf_eager.py, shelf_functions.py and shelf_lazy.py (the last
one with the same eager file re-run under -X lazy_imports=all) across
the four commands used in the article, 5 warm-up runs and N measured
runs each, alternating, and prints the median per cell.

Usage: python3.15 bench.py <python-executable> <measured-runs>
"""
import statistics as stats
import subprocess
import sys
import time

PY = sys.argv[1] if len(sys.argv) > 1 else sys.executable
RUNS = int(sys.argv[2]) if len(sys.argv) > 2 else 40

COMMANDS = {
    "--version": ["--version"],
    "list": ["list", "--file", "books.json"],
    "csv": ["csv", "--file", "books.json"],
    "backup": ["backup", "--file", "books.json"],
}

VARIANTS = {
    "eager": (PY, "shelf_eager.py", []),
    "functions": (PY, "shelf_functions.py", []),
    "lazy": (PY, "shelf_lazy.py", []),
    "eager+lazy_imports=all": (PY, "shelf_eager.py", ["-X", "lazy_imports=all"]),
}


def run_once(python, script, flags, args):
    start = time.perf_counter()
    subprocess.run([python, *flags, script, *args], stdout=subprocess.DEVNULL,
                    stderr=subprocess.PIPE, check=True)
    return (time.perf_counter() - start) * 1000


def median_time(python, script, flags, args, runs):
    for _ in range(5):
        run_once(python, script, flags, args)
    samples = [run_once(python, script, flags, args) for _ in range(runs)]
    return stats.median(samples)


if __name__ == "__main__":
    header = f"{'command':10s}" + "".join(f"{name:>24s}" for name in VARIANTS)
    print(header)
    for cmd_name, args in COMMANDS.items():
        row = f"{cmd_name:10s}"
        for python, script, flags in VARIANTS.values():
            ms = median_time(python, script, flags, args, RUNS)
            row += f"{ms:23.0f}m"
        print(row)
