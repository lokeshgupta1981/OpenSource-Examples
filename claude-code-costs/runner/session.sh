#!/usr/bin/env bash
# Usage: session.sh <rep>
# Builds an unrelated history session, then runs the same clear task
# (F) continued in that session and (G) after /compact. Fresh runs are config D.
set -u
REP=$1
HERE=$(cd "$(dirname "$0")/.." && pwd)
export RUN_DIR=/tmp/orderdesk-run2/orderdesk
OUT=${OUT_DIR:-$HERE/results}
ARGS=(--output-format json --model opus --permission-mode acceptEdits --allowedTools "Bash Read Edit Write Glob Grep Agent")
rm -rf "$RUN_DIR"; mkdir -p "$RUN_DIR"
cp -r "$HERE/orderdesk/." "$RUN_DIR/"; rm -rf "$RUN_DIR/target"; cp -r "$HERE/setups/lean/." "$RUN_DIR/"
cd "$RUN_DIR" && git init -q && git add -A && git -c user.name=t -c user.email=t@t commit -qm start
claude -p "$(cat $HERE/prompts/history.txt)" "${ARGS[@]}" > "$OUT/H$REP-history1.json"
SID=$(python3 -c "import json;print(json.load(open('$OUT/H$REP-history1.json'))['session_id'])")
claude -p "$(cat $HERE/prompts/history2.txt)" --resume "$SID" "${ARGS[@]}" > "$OUT/H$REP-history2.json"
SID=$(python3 -c "import json;print(json.load(open('$OUT/H$REP-history2.json'))['session_id'])")
echo "history session $SID"
git checkout -q . ; git clean -qfd -e target
KEEP_DIR=1 "$HERE/runner/run.sh" F$REP-long-session "" "$HERE/prompts/clear.txt" --resume "$SID" --fork-session
git checkout -q . ; git clean -qfd -e target
claude -p "/compact" --resume "$SID" --fork-session "${ARGS[@]}" > "$OUT/G$REP-compact-step.json"
CSID=$(python3 -c "import json;print(json.load(open('$OUT/G$REP-compact-step.json'))['session_id'])")
KEEP_DIR=1 "$HERE/runner/run.sh" G$REP-after-compact "" "$HERE/prompts/clear.txt" --resume "$CSID"
