"""Splits five files into chunks and ranks them by TF-IDF overlap with the
question, instead of pasting the whole files into the request."""
import math
import re
from collections import Counter
from ctx import tokens
from scenario import pasted_files

WORD = re.compile(r"[a-zA-Z_]+")


def chunks(name: str, text: str, size: int = 30):
    lines = text.splitlines()
    for start in range(0, len(lines), size):
        block = "\n".join(lines[start:start + size])
        yield f"{name}:{start + 1}", block


def _words(text: str) -> list:
    return [w.lower() for w in WORD.findall(text)]


def top_chunks(question: str, corpus: list, k: int = 3) -> list:
    q_words = set(_words(question))
    doc_freq = Counter()
    docs = []
    for label, text in corpus:
        words = set(_words(text))
        docs.append((label, text, words))
        for w in words & q_words:
            doc_freq[w] += 1
    n = len(docs)
    scores = []
    for label, text, words in docs:
        score = 0.0
        for w in q_words & words:
            idf = math.log((n + 1) / (doc_freq[w] + 1)) + 1
            score += idf
        scores.append((score, label, text))
    scores.sort(key=lambda x: x[0], reverse=True)
    return [(label, text) for _, label, text in scores[:k]]


if __name__ == "__main__":
    whole = pasted_files()
    corpus = [c for name, text in whole.items() for c in chunks(name, text)]
    question = "How does csv.Sniffer detect the delimiter of a CSV file?"
    best = top_chunks(question, corpus)

    pasted = sum(tokens(text) for text in whole.values())
    retrieved = sum(tokens(text) for _, text in best)

    print("files", len(whole), "pasted", pasted, "retrieved", retrieved)
    print("chunks", [label for label, _ in best])
