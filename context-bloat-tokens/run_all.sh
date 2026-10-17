#!/bin/sh
set -e
for f in 0*.py; do
  echo "=== $f ==="
  python3 "$f"
  echo
done
