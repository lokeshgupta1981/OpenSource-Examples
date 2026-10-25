#!/bin/bash
# PreToolUse hook: when Claude runs a plain Maven test command,
# keep the failure details and drop the rest of the build log.
input=$(cat)
cmd=$(echo "$input" | jq -r '.tool_input.command')

pattern='^(mvn|./mvnw)( -q)? test( -Dtest=[A-Za-z0-9_#.,*]+)?$'
if [[ "$cmd" =~ $pattern ]]; then
  filtered="$cmd 2>&1 | grep -E -A 8 '(FAIL|ERROR|Tests run:)' | head -80"
  echo "$input" | jq --arg c "$filtered" \
    '{hookSpecificOutput: {hookEventName: "PreToolUse", updatedInput: (.tool_input + {command: $c})}}'
else
  echo "{}"
fi
