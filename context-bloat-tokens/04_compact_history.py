"""Replaces the old messages of a session with one summary and keeps the last few
messages word for word."""
from typing import Callable
from ctx import tokens_json
from scenario import conversation_history


def compact(messages: list, keep_last: int, summarize: Callable[[list], str]) -> list:
    if len(messages) <= keep_last:
        return messages
    old, recent = messages[:-keep_last], messages[-keep_last:]
    summary = {"role": "user", "content": "Summary of the earlier conversation.\n" + summarize(old)}
    return [summary] + recent


def summarize_with_notes(old_messages: list) -> str:
    return (
        "Goal: fix the renewal check for loans with holds.\n"
        "Decision: renew_loan() raises RenewalError when a hold exists; the limit "
        "of two renewals is in loans/rules.py.\n"
        "Open: test_renew_with_hold now passes after the message was corrected to "
        "include the loan id."
    )


if __name__ == "__main__":
    messages = conversation_history()
    before = tokens_json(messages)
    compacted = compact(messages, keep_last=6, summarize=summarize_with_notes)
    after = tokens_json(compacted)

    print("messages", len(messages), "->", len(compacted))
    print("tokens", before, "->", after)
    print()
    print(compacted[0]["content"])
