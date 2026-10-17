"""Counts tokens with the o200k_base encoding, used by every script in this example."""
import json
import tiktoken

ENCODING = tiktoken.get_encoding("o200k_base")


def tokens(text: str) -> int:
    return len(ENCODING.encode(text))


def tokens_json(value) -> int:
    return tokens(json.dumps(value, separators=(",", ":")))
