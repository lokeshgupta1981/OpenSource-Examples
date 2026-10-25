Source code for the article [How to Reduce Claude Code Costs Without Losing Quality](https://howtodoinjava.com/ai/reduce-claude-code-costs/)

The folder holds one small Spring Boot project and the setups we measured it with.

- `orderdesk/`: the OrderDesk order service (Java 21, Spring Boot 4.1.2). *OrderValidatorTest.rejectsNegativeQuantity* fails on purpose.
- `setups/baseline/`: a 417-line *CLAUDE.md* with everything inline.
- `setups/lean-noisy/`: a 29-line *CLAUDE.md*, with the API catalogue and style guide in *docs/* and the release runbook in a skill.
- `setups/lean/`: the same lean file with targeted test commands and class locations.
- `setups/lean-hook/`: the lean setup plus a PreToolUse hook that trims Maven test output.
- `setups/lean-subagent/`: the lean setup plus a read-only *code-locator* subagent on Haiku.
- `prompts/`: the vague and the specific task prompts, and the prompts that build a long session.
- `runner/run.sh`: copies the project to a fixed folder, applies a setup, runs one headless Claude Code task and then the quality gate (full test suite passes, no test file changed).
- `runner/session.sh`: runs the same task in a long session and after `/compact`.
- `runner/session-cold.sh`: runs the same task in a long session after its 5-minute cache has expired.
- `runner/analyze.py`: prints the median, lowest and highest cost per setup (`python3 runner/analyze.py results`).
- `results/`: the JSON usage report, diff and gate log of every run, and *summary.csv*.

Run one measurement (Claude Code 2.1.310, Maven 3.9, JDK 21):

```bash
./runner/run.sh A1-baseline setups/baseline prompts/vague.txt
```

Each run spends real tokens on your account. The cost figures are Claude Code's own list-price estimates.
