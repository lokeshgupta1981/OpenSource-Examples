"""Compares a single agent that reads five files itself against a subagent that
reads them in its own context and returns only a short report."""
from ctx import tokens


class Context:
    def __init__(self, name: str):
        self.name = name
        self.messages: list[str] = []

    def add(self, text: str):
        self.messages.append(text)

    @property
    def size(self) -> int:
        return sum(tokens(m) for m in self.messages)


def run_subagent(task: str, files: dict):
    sub = Context("subagent")
    sub.add(task)
    for text in files.values():
        sub.add(text)
    # A model would read sub's context and write this report; here it is fixed,
    # so the script needs no API key. sub.size is still the tokens it read.
    report = (
        "csv.Sniffer.sniff() guesses the delimiter by counting candidate "
        "characters outside quoted fields and picking the most frequent one "
        "from the preferred list; see csv.py around line 244."
    )
    return report, sub.size


if __name__ == "__main__":
    from scenario import pasted_files

    FILES = pasted_files()
    task = "How does csv.Sniffer detect the delimiter?"

    single = Context("single agent")
    single.add(task)
    for text in FILES.values():
        single.add(text)

    main = Context("main agent")
    main.add(task)
    report, sub_size = run_subagent(task, FILES)
    main.add(report)

    print("single agent context:", single.size, "tokens")
    print("subagent context:", sub_size, "tokens")
    print("main agent context:", main.size, "tokens")
