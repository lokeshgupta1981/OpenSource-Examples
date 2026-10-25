#!/usr/bin/env bash
# Usage: run.sh <label> <setup-dir> <prompt-file> [extra claude args...]
# Copies the project to a fixed path, applies a setup, runs one headless Claude Code task,
# then runs the quality gate (full test suite and a check that no test file changed).
set -u
LABEL=$1; SETUP=$2; PROMPT_FILE=$3; shift 3
HERE=$(cd "$(dirname "$0")/.." && pwd)
RUN_DIR=${RUN_DIR:-/tmp/orderdesk-run/orderdesk}
OUT=${OUT_DIR:-$HERE/results}
mkdir -p "$OUT"
if [ -z "${KEEP_DIR:-}" ]; then
  rm -rf "$RUN_DIR"; mkdir -p "$RUN_DIR"
  cp -r "$HERE/orderdesk/." "$RUN_DIR/"
  rm -rf "$RUN_DIR/target"
  [ -n "$SETUP" ] && cp -r "$SETUP/." "$RUN_DIR/"
  (cd "$RUN_DIR" && git init -q && git add -A && git -c user.name=t -c user.email=t@t commit -qm start)
fi
cd "$RUN_DIR"
START=$(date +%s)
claude -p "$(cat "$PROMPT_FILE")" --output-format json --model opus \
  --permission-mode acceptEdits \
  --allowedTools "Bash Read Edit Write Glob Grep Agent" "$@" > "$OUT/$LABEL.json" 2> "$OUT/$LABEL.err"
END=$(date +%s)
if mvn -q test > "$OUT/$LABEL.gate.log" 2>&1; then GATE=pass; else GATE=fail; fi
TESTS_CHANGED=$(git diff --name-only HEAD -- src/test | wc -l)
CHANGED=$(git diff --name-only HEAD | tr '\n' ' ')
git diff HEAD > "$OUT/$LABEL.diff"
python3 - "$OUT/$LABEL.json" "$LABEL" "$GATE" "$TESTS_CHANGED" "$CHANGED" "$((END-START))" >> "$OUT/summary.csv" <<'PY'
import json,sys
f,label,gate,tch,changed,secs=sys.argv[1:]
try: d=json.load(open(f))
except Exception as e: print(f"{label},ERROR,,,,,,,{gate},{tch},{changed.strip()},{secs}"); sys.exit()
u=d.get("usage",{})
print(",".join(map(str,[label,round(d.get("total_cost_usd",0),4),d.get("num_turns",""),
  u.get("input_tokens",0),u.get("cache_creation_input_tokens",0),u.get("cache_read_input_tokens",0),
  u.get("output_tokens",0),d.get("session_id",""),gate,tch,changed.strip(),secs])))
PY
tail -1 "$OUT/summary.csv"
