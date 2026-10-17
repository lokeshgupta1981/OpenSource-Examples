"""Builds the same request twice: before, with everything pasted in full, and
after, with trimming, retrieval, compaction and on-demand loading applied."""
from ctx import tokens, tokens_json
from catalog_tools import TOOLS
from scenario import SYSTEM_PROMPT, pasted_files, test_log, loans_response, conversation_history

from importlib import import_module

trim = import_module("03_trim_output")
compact_mod = import_module("04_compact_history")
retrieve = import_module("05_retrieve_instead_of_paste")
on_demand = import_module("06_load_on_demand")

system_prompt_t = tokens(SYSTEM_PROMPT)

# before
tools_before = tokens_json(TOOLS)
history_before_msgs = conversation_history()
history_before_t = tokens_json(history_before_msgs)
log = test_log()
rows = loans_response()
results_before_t = tokens(log) + tokens_json(rows)
files = pasted_files()
files_before_t = sum(tokens(t) for t in files.values())
total_before = system_prompt_t + tools_before + history_before_t + results_before_t + files_before_t

# after
index = on_demand.tokens_json(on_demand.tool_index())
loaded = on_demand.search_tools("renew a loan for a member")
tools_after = index + on_demand.tokens_json(loaded)

compacted = compact_mod.compact(history_before_msgs, keep_last=6,
                                 summarize=compact_mod.summarize_with_notes)
history_after_t = tokens_json(compacted)

log_trimmed = trim.trim_test_log(log)
slim_rows = trim.trim_rows(rows, ["id", "member_id", "due", "status"])
results_after_t = tokens(log_trimmed) + tokens(slim_rows)

question = "How does csv.Sniffer detect the delimiter of a CSV file?"
corpus = [c for name, text in files.items() for c in retrieve.chunks(name, text)]
best = retrieve.top_chunks(question, corpus)
files_after_t = sum(tokens(text) for _, text in best)

total_after = system_prompt_t + tools_after + history_after_t + results_after_t + files_after_t


def pct(before, after):
    if before == 0:
        return 0.0
    return round((1 - after / before) * 100, 1)


rows_out = [
    ("System prompt", system_prompt_t, system_prompt_t),
    ("Tool definitions", tools_before, tools_after),
    ("Conversation history", history_before_t, history_after_t),
    ("Tool results", results_before_t, results_after_t),
    ("Code context", files_before_t, files_after_t),
]

for name, b, a in rows_out:
    print(f"{name}: before {b} after {a} reduction {pct(b, a)}%")
print(f"Total: before {total_before} after {total_after} reduction {pct(total_before, total_after)}%")
