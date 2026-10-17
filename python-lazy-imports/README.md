Source code for the article https://howtodoinjava.com/python/python-lazy-imports/

Versions used: the function-local-import and eager comparisons run on any Python
3.13+ with the standard library only. The `lazy import` keyword (PEP 810) needs
Python 3.15 or later; this repo's own measurements were taken on Python
3.15.0b4 (the latest build available while preparing this example; Python
3.15.0 final shipped October 9, 2026, with the same syntax and semantics).

Files:
- shelf_common.py - the shared argument parser and JSON loader
- shelf_eager.py - all ten modules imported at the top of the file
- shelf_functions.py - each subcommand imports its own modules, inside its function
- shelf_lazy.py - the same top-level imports as shelf_eager.py, with the `lazy`
  keyword added to every import a run might not need (needs Python 3.15+)
- bench.py - times all three files, plus shelf_eager.py run under
  `-X lazy_imports=all`, across four commands, and prints the median per cell
- books.json - the sample book catalog the tool reads

Run commands:

    python3 shelf_eager.py list
    python3 shelf_eager.py stats
    python3 shelf_functions.py csv
    python3.15 shelf_lazy.py backup

    # benchmark (needs Python 3.15+ for the full comparison)
    python3.15 bench.py python3.15 40
