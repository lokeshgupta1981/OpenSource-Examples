import statistics
import sys
import time
from concurrent.futures import ThreadPoolExecutor

LIMIT = 100_000
TASKS = 4
RUNS = 15


def count_primes(limit):
    count = 0
    for n in range(2, limit):
        if all(n % d for d in range(2, int(n ** 0.5) + 1)):
            count += 1
    return count


def run(workers):
    start = time.perf_counter()
    with ThreadPoolExecutor(max_workers=workers) as pool:
        results = list(pool.map(count_primes, [LIMIT] * TASKS))
    elapsed = time.perf_counter() - start
    assert results == [9592] * TASKS
    return elapsed


print(f"Python {sys.version.split()[0]} | GIL enabled: {sys._is_gil_enabled()}")

for workers in (1, 2):
    times = [run(workers) for _ in range(RUNS)]
    print(f"{workers} thread(s): median {statistics.median(times):.2f} s, best {min(times):.2f} s")
