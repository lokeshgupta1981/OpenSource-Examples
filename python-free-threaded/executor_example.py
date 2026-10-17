from concurrent.futures import ThreadPoolExecutor


def count_primes(limit):
    count = 0
    for n in range(2, limit):
        if all(n % d for d in range(2, int(n ** 0.5) + 1)):
            count += 1
    return count


limits = [10_000, 20_000, 30_000, 40_000]

with ThreadPoolExecutor(max_workers=4) as pool:
    results = list(pool.map(count_primes, limits))   # each task returns its own result

print(dict(zip(limits, results)))
