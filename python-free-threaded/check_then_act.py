import sys
import threading
import time

TICKETS = 100
BUYERS = 8

tickets_left = TICKETS
sold = 0
lock = threading.Lock()


def buy_unsafe():
    global tickets_left, sold
    for _ in range(TICKETS):
        if tickets_left > 0:        # check
            time.sleep(0)            # another thread can run here
            tickets_left -= 1        # act
            sold += 1


def buy_safe():
    global tickets_left, sold
    for _ in range(TICKETS):
        with lock:
            if tickets_left > 0:
                time.sleep(0)
                tickets_left -= 1
                sold += 1


def run(target):
    threads = [threading.Thread(target=target) for _ in range(BUYERS)]
    for t in threads:
        t.start()
    for t in threads:
        t.join()


print("GIL enabled:", sys._is_gil_enabled())
print("Tickets available:", TICKETS)

run(buy_unsafe)
print("Without a lock, sold/left:", (sold, tickets_left))

tickets_left = TICKETS
sold = 0
run(buy_safe)
print("With a lock, sold/left:", (sold, tickets_left))
