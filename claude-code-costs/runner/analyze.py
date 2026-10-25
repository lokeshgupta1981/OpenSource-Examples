"""Summarize the measured runs in results/.

The cost of a run is computed from the run's own usage block at Claude Opus 5.5 list prices
(USD per million tokens). For a run that resumes a session, Claude Code's total_cost_usd is
the whole session's cost, so we use the run's own usage instead. For other runs the two match,
and total_cost_usd also covers subagent models, so we use it.
"""
import csv
import json
import statistics
import sys
from pathlib import Path

PRICE = {"input": 4.00, "output": 20.00, "write_5m": 5.00, "write_1h": 8.00, "read": 0.20}

results = Path(sys.argv[1] if len(sys.argv) > 1 else "results")
rows = {r["label"]: r for r in csv.DictReader(open(results / "summary.csv"))}


def own_cost(usage):
    cc = usage.get("cache_creation", {})
    return (usage.get("input_tokens", 0) * PRICE["input"]
            + usage.get("output_tokens", 0) * PRICE["output"]
            + cc.get("ephemeral_5m_input_tokens", 0) * PRICE["write_5m"]
            + cc.get("ephemeral_1h_input_tokens", 0) * PRICE["write_1h"]
            + usage.get("cache_read_input_tokens", 0) * PRICE["read"]) / 1_000_000


groups = {}
for label, row in rows.items():
    data = json.load(open(results / f"{label}.json"))
    usage = data["usage"]
    resumed = label[0] in "FGK"
    cost = own_cost(usage) if resumed else data["total_cost_usd"]
    groups.setdefault(label[0] + label[2:] if label[1].isdigit() else label, []).append({
        "label": label, "cost": cost, "turns": data.get("num_turns"),
        "write": usage.get("cache_creation_input_tokens", 0),
        "read": usage.get("cache_read_input_tokens", 0),
        "output": usage.get("output_tokens", 0),
        "gate": row["gate"], "tests_changed": row["tests_changed"], "secs": int(row["secs"]),
    })


def med(values):
    return statistics.median(values)


print(f"{'config':24} {'n':>2} {'cost med':>9} {'min':>7} {'max':>7} {'turns':>5} "
      f"{'write':>7} {'read':>8} {'output':>6} gate")
for name in sorted(groups):
    g = groups[name]
    costs = [x["cost"] for x in g]
    print(f"{name:24} {len(g):>2} {med(costs):9.4f} {min(costs):7.4f} {max(costs):7.4f} "
          f"{med([x['turns'] for x in g]):>5} {med([x['write'] for x in g]):>7.0f} "
          f"{med([x['read'] for x in g]):>8.0f} {med([x['output'] for x in g]):>6.0f} "
          f"{sum(x['gate'] == 'pass' and x['tests_changed'] == '0' for x in g)}/{len(g)}")
    for x in g:
        print(f"    {x['label']:22} {x['cost']:.4f} turns={x['turns']} write={x['write']} "
              f"read={x['read']} out={x['output']} {x['gate']} {x['secs']}s")
