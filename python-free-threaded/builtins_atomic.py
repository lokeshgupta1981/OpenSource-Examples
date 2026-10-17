import sys
import threading

THREADS = 8
LOOPS = 100_000

items = []
counts = {"apple": 0}
safe_counts = {"apple": 0}
lock = threading.Lock()


def append_items():
    for i in range(LOOPS):
        items.append(i)   # one operation, protected by the list's own lock


def count_unsafe():
    for _ in range(LOOPS):
        counts["apple"] = counts["apple"] + 1   # read, add, write = 3 steps


def count_safe():
    global safe_counts
    for _ in range(LOOPS):
        with lock:
            safe_counts["apple"] = safe_counts["apple"] + 1


def run(target):
    threads = [threading.Thread(target=target) for _ in range(THREADS)]
    for t in threads:
        t.start()
    for t in threads:
        t.join()


print("GIL enabled:", sys._is_gil_enabled())
print("Expected:", THREADS * LOOPS)

run(append_items)
print("list.append, no lock:", len(items))

run(count_unsafe)
print("dict read-add-write, no lock:", counts["apple"])

run(count_safe)
print("dict read-add-write, lock:", safe_counts["apple"])
