"""The data of one coding-agent session on a library catalog service: a test log,
a list of loan rows, a chat history and the five source files an agent pasted in.
Every function returns realistic text built by code, not hand-typed filler, so the
sizes below come from running this module and not from a guess."""
import importlib
import inspect
import json
import random
from pathlib import Path

SYSTEM_PROMPT = """You are a coding agent working in the library-catalog-service repository.
Follow the existing code style: 4-space indentation, type hints on every function,
and docstrings on every public function. Run the test suite after any change to
loans/rules.py or loans/service.py. Prefer small, reviewable commits. Never commit
directly to the main branch; open a pull request instead. When a test fails, read
the stack trace before changing code, and add a regression test for the fix."""

STDLIB_FILES = ["csv", "json", "http.client", "argparse", "shutil"]


def pasted_files() -> dict:
    """Five files of the Python standard library, standing in for the repository
    of the catalog service: real code every reader already has on disk."""
    out = {}
    for name in STDLIB_FILES:
        mod = importlib.import_module(name)
        out[name + ".py"] = inspect.getsource(mod)
    return out


def test_log(total=312, failed=1, seed=7) -> str:
    """A pytest run with one failing test, sized the way a real suite's output is:
    one line per passed test plus a failure section for the one that broke."""
    rng = random.Random(seed)
    test_names = [
        f"test_{area}_{action}"
        for area in ["loan", "member", "hold", "fee", "branch", "catalog", "renewal"]
        for action in ["create", "update", "delete", "list", "validate", "format",
                        "serialize", "lookup", "search", "sort", "filter", "notify",
                        "schedule", "cancel", "transfer", "reconcile", "archive",
                        "expire", "escalate", "audit", "sync", "merge", "split",
                        "retry", "rollback", "commit", "publish", "subscribe",
                        "throttle", "paginate", "cache", "invalidate", "refresh",
                        "resolve", "queue", "dequeue", "batch", "stream", "export",
                        "import", "validate_schema", "normalize", "dedupe", "backfill"]
    ]
    rng.shuffle(test_names)
    test_names = test_names[:total - 1] + ["test_renew_with_hold"]
    lines = []
    for name in test_names:
        if name == "test_renew_with_hold":
            continue
        lines.append(f"tests/test_loans.py::{name} PASSED")
    failure = (
        "================================== FAILURES ==================================\n"
        "______________________________ test_renew_with_hold ______________________________\n"
        "    def test_renew_with_hold():\n"
        "        loan = make_loan(hold=True)\n"
        "> renew_loan(loan.id)\n"
        "E RenewalError: loan 17 has an active hold\n"
        "tests/test_loans.py:208: RenewalError\n"
    )
    summary = f"=========== {failed} failed, {total - failed} passed in 14.21s ===========\n"
    return "\n".join(lines) + "\n" + failure + summary


def loans_response(count=120, seed=11) -> list:
    """The JSON rows a list_loans call on a busy branch returns: one dict per
    loan, with the fields a real catalog API would include."""
    rng = random.Random(seed)
    statuses = ["active", "active", "active", "overdue", "returned"]
    rows = []
    for i in range(1, count + 1):
        rows.append({
            "id": f"L{i:04d}",
            "member_id": f"M{rng.randint(1, 60):03d}",
            "book_id": f"B{rng.randint(1, 400):04d}",
            "checked_out": f"2026-{rng.randint(9, 10):02d}-{rng.randint(1, 28):02d}",
            "due": f"2026-11-{rng.randint(1, 28):02d}",
            "status": rng.choice(statuses),
            "branch_id": f"BR-{rng.randint(1, 5)}",
            "renewal_count": rng.randint(0, 2),
        })
    return rows


def conversation_history(turns=30) -> list:
    """60 messages (30 user/assistant turns) of a session that starts from a bug
    report and ends at the renewal fix, the shape an agent loop actually sends."""
    messages = [
        {"role": "user", "content": "The renewal endpoint is letting members renew loans "
                                     "that have an active hold. Fix it."},
        {"role": "assistant", "content": "I'll look at loans/service.py and the test file "
                                          "to see how renew_loan() checks holds today."},
    ]
    steps = [
        ("Reading loans/service.py to find renew_loan().",
         "renew_loan() currently only checks the renewal_count limit; it never calls "
         "list_holds() for the loan's book_id."),
        ("Reading loans/rules.py for the renewal limit constant.",
         "MAX_RENEWALS = 2 is defined in loans/rules.py, alongside the overdue fee rate."),
        ("Adding a hold check before the renewal_count check.",
         "Added: if list_holds(loan.book_id): raise RenewalError('an active hold exists')."),
        ("Running the test suite.",
         "311 passed, 1 failed: test_renew_with_hold now fails on the message text, "
         "not the behavior."),
        ("Reading tests/test_loans.py:208 to see the expected message.",
         "The test expects the exact string 'loan 17 has an active hold', with the "
         "loan id interpolated."),
        ("Updating the error message to include the loan id.",
         "Changed to f'loan {loan.id} has an active hold'."),
        ("Running the test suite again.",
         "312 passed, 0 failed."),
        ("Checking for other callers of renew_loan().",
         "Only the REST handler in api/loans.py calls it, and it already surfaces "
         "RenewalError as a 409 response."),
        ("Checking the CHANGELOG convention for this repo.",
         "Entries are one line under the current unreleased heading, past tense."),
        ("Drafting the CHANGELOG line and the commit message.",
         "'Reject loan renewal when the book has an active hold.' matches the style "
         "of the last five entries."),
        ("Re-running the full suite once more before opening the PR.",
         "312 passed, 0 failed, 14.21s."),
        ("Opening the pull request.",
         "PR #482 opened against main with the diff, the changelog line and a new "
         "regression test."),
    ]
    for prompt, result in steps:
        messages.append({"role": "assistant", "content": f"Tool call: run_step. {prompt}"})
        messages.append({"role": "tool", "content": result})
    while len(messages) < turns * 2:
        messages.append({"role": "assistant", "content": "Tool call: run_tests."})
        messages.append({"role": "tool", "content": "312 passed, 0 failed, 14.21s."})
    return messages[: turns * 2]
