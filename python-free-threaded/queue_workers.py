import queue
import threading

WORKERS = 4
orders = queue.Queue()
totals = queue.Queue()


def worker():
    while True:
        order = orders.get()
        if order is None:    # sentinel, tells this worker to stop
            orders.task_done()
            break
        totals.put((order, order * 2))
        orders.task_done()


threads = [threading.Thread(target=worker) for _ in range(WORKERS)]
for t in threads:
    t.start()

for n in range(1, 9):
    orders.put(n)
for _ in range(WORKERS):
    orders.put(None)

orders.join()
for t in threads:
    t.join()

results = []
while not totals.empty():
    results.append(totals.get())

print(sorted(results))
