import sys
import sysconfig
import threading

free_threaded_build = sysconfig.get_config_var("Py_GIL_DISABLED") == 1  # True on python3.14t, False on python3.14
gil_enabled = sys._is_gil_enabled()  # False on python3.14t, True on python3.14

counter = 0
lock = threading.Lock()


def increment():
    global counter
    with lock:  # without the lock, counter += 1 can lose updates
        counter += 1


threads = [threading.Thread(target=increment) for _ in range(4)]
for t in threads:
    t.start()
for t in threads:
    t.join()

print(free_threaded_build, gil_enabled, counter)  # counter is 4 on both builds
