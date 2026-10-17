Source code for the article https://howtodoinjava.com/python/python-jit-compiler/

Versions used: a from-source build of CPython 3.14.0, configured with
`--enable-experimental-jit=yes-off` and built with LLVM/clang 19 (the version
the CPython 3.13 to 3.15 build scripts require). The baseline comparison in
the article used the distribution-packaged Python 3.13.16, which has no JIT
support at all (`hasattr(sys, "_jit")` is `False`).

Files:
- recipe_jit_bench.py - the hot loop (scaling a recipe's flour amount by a
  changing batch factor, 20,000,000 times) and the sys._jit check, printing
  one run's elapsed time
- bench.py - runs recipe_jit_bench.py 5 times in a row and prints the median,
  min and max elapsed time

Build a JIT-enabled interpreter first (see the article's section 6), then run:

```bash
PYTHON_JIT=0 /opt/py314-jit/bin/python3 bench.py   # JIT off
PYTHON_JIT=1 /opt/py314-jit/bin/python3 bench.py   # JIT on
```

The numbers printed in the article (median 1.9603s off, 1.8153s on, on a
shared 2-vCPU Linux sandbox, five runs each) came from running
recipe_jit_bench.py directly five times under each setting, not from a single
run of bench.py. The per-run range in both groups overlaps, which is the
article's point: on a shared machine, a single comparison is not enough to
credit the difference to the JIT.
