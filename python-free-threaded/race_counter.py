import sys
import threading

THREADS = 4
LOOPS = 250_000

counter = 0
safe_counter = 0
lock = threading.Lock()


def unsafe_increment():
    global counter
    for _ in range(LOOPS):
        counter += 1


def safe_increment():
    global safe_counter
    for _ in range(LOOPS):
        with lock:
            safe_counter += 1


def run(target):
    threads = [threading.Thread(target=target) for _ in range(THREADS)]
    for t in threads:
        t.start()
    for t in threads:
        t.join()


print("GIL enabled:", sys._is_gil_enabled())
print("Expected:", THREADS * LOOPS)

run(unsafe_increment)
print("Without a lock:", counter)

run(safe_increment)
print("With a lock:", safe_counter)
