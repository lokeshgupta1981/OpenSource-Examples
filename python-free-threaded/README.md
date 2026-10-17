Source code for the article [Free-Threaded Python (No-GIL): Writing Thread-Safe Code](https://howtodoinjava.com/python/free-threaded-python-no-gil-thread-safe-code/)

Versions: Python 3.14.6 (standard and free-threaded builds, `python3.14` and `python3.14t`),
GCC for the C extensions in `extensions/`.

Run any script with either build to compare:

```bash
python3.14 race_counter.py
python3.14t race_counter.py
```

Build the two extensions before running `extensions/use_extensions.py`:

```bash
cd extensions
python3.14t-config --includes
gcc -shared -fPIC $(python3.14t-config --includes) -o legacy$(python3.14t-config --extension-suffix) legacy.c
gcc -shared -fPIC $(python3.14t-config --includes) -o modern$(python3.14t-config --extension-suffix) modern.c
python3.14t use_extensions.py
```

Files:

- `quick_reference.py` - the intro example, a locked counter.
- `check_build.py` - prints which build is running and whether the GIL is on.
- `race_counter.py` - the lost-update race on a shared counter, with and without a lock.
- `check_then_act.py` - the ticket-selling check-then-act race.
- `builtins_atomic.py` - `list.append()` versus a `dict` read-modify-write under load.
- `rlock_example.py` - a `Playlist` class that needs `threading.RLock`.
- `queue_workers.py` - passing work between threads with `queue.Queue`.
- `executor_example.py` - collecting results with `ThreadPoolExecutor`.
- `benchmark.py` - the 1-thread vs 2-thread CPU-bound timing.
- `extensions/legacy.c`, `extensions/modern.c` - a C extension that does not declare
  `Py_mod_gil` and one that does, plus `extensions/use_extensions.py` to import both.
