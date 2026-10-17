"""Trims the test log to its failures and the loan rows to the fields a renewal
question actually needs."""
import json
from ctx import tokens, tokens_json
from scenario import test_log, loans_response


def trim_test_log(log: str) -> str:
    lines = log.splitlines()
    out = []
    in_failure = False
    for line in lines:
        if line.startswith("=") and "FAILURES" in line:
            in_failure = True
        if line.startswith("===========") and "passed" in line:
            out.append(line)
            continue
        if in_failure:
            out.append(line)
    return "\n".join(out)


def trim_rows(rows: list, fields: list, limit: int = 5) -> str:
    kept = [{f: row[f] for f in fields} for row in rows[:limit]]
    omitted = len(rows) - len(kept)
    return json.dumps({"rows": kept, "omitted": omitted, "total": len(rows)}, separators=(",", ":"))


if __name__ == "__main__":
    log = test_log()
    rows = loans_response()

    log_trimmed = trim_test_log(log)
    slim = trim_rows(rows, ["id", "member_id", "due", "status"])

    print("log before", tokens(log), "after", tokens(log_trimmed))
    print("rows before", tokens_json(rows), "after", tokens(slim))
    print()
    print(log_trimmed)
    print()
    print(slim)
