#!/usr/bin/env bash
# Usage: session-cold.sh <rep>
# Same history as session.sh, but with the 5-minute cache lifetime and a 6-minute pause
# before the task, so the cached prefix has expired when the task starts.
set -u
REP=$1
HERE=$(cd "$(dirname "$0")/.." && pwd)
export RUN_DIR=/tmp/orderdesk-run3/orderdesk
export FORCE_PROMPT_CACHING_5M=1
OUT=${OUT_DIR:-$HERE/results}
ARGS=(--output-format json --model opus --permission-mode acceptEdits --allowedTools "Bash Read Edit Write Glob Grep Agent")
rm -rf "$RUN_DIR"; mkdir -p "$RUN_DIR"
cp -r "$HERE/orderdesk/." "$RUN_DIR/"; rm -rf "$RUN_DIR/target"; cp -r "$HERE/setups/lean/." "$RUN_DIR/"
cd "$RUN_DIR" && git init -q && git add -A && git -c user.name=t -c user.email=t@t commit -qm start
claude -p "$(cat $HERE/prompts/history.txt)" "${ARGS[@]}" > "$OUT/K$REP-history1.json"
SID=$(python3 -c "import json;print(json.load(open('$OUT/K$REP-history1.json'))['session_id'])")
claude -p "$(cat $HERE/prompts/history2.txt)" --resume "$SID" "${ARGS[@]}" > "$OUT/K$REP-history2.json"
SID=$(python3 -c "import json;print(json.load(open('$OUT/K$REP-history2.json'))['session_id'])")
echo "cold history session $SID"
git checkout -q . ; git clean -qfd -e target
sleep 370
KEEP_DIR=1 "$HERE/runner/run.sh" K$REP-expired-cache "" "$HERE/prompts/clear.txt" --resume "$SID" --fork-session
