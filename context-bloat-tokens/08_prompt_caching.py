"""Hashes the stable prefix of a request (tool definitions plus system prompt)
and shows that one added space breaks the hash, which is a cache miss."""
import copy
import hashlib
import json
from ctx import tokens, tokens_json
from catalog_tools import TOOLS
from scenario import SYSTEM_PROMPT

WRITE = 1.25  # Anthropic five-minute cache write, times the base input price
READ = 0.1    # Anthropic cache read, times the base input price


def prefix_key(tools: list, system_prompt: str) -> str:
    blob = json.dumps(tools, separators=(",", ":")) + "\n" + system_prompt
    return hashlib.sha256(blob.encode()).hexdigest()


if __name__ == "__main__":
    changed = copy.deepcopy(TOOLS)
    changed[0]["description"] = changed[0]["description"] + " "  # one space added

    key = prefix_key(TOOLS, SYSTEM_PROMPT)
    same = key == prefix_key(TOOLS, SYSTEM_PROMPT)
    edited = key == prefix_key(changed, SYSTEM_PROMPT)

    stable = tokens_json(TOOLS) + tokens(SYSTEM_PROMPT)
    turns = 20
    without_cache = stable * turns
    with_cache = stable * WRITE + stable * READ * (turns - 1)

    print("stable prefix:", stable, "tokens")
    print("same prefix:", same)
    print("one space added to one description:", edited)
    print(f"{turns} turns without cache: {without_cache} with cache: {round(with_cache)}")
