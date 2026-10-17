"""Counts one sentence with o200k_base, then compares three languages against the
older cl100k_base encoding."""
import tiktoken

text = "Context bloat makes every agent turn slower and more expensive."

encoding = tiktoken.get_encoding("o200k_base")  # downloads the vocabulary once
ids = encoding.encode(text)  # a list of token ids
count = len(ids)  # count = 13
pieces = [encoding.decode([i]) for i in ids]
print("count", count)
print("pieces", pieces)

samples = {
    "english": "Context bloat makes every agent turn slower and more expensive.",
    "german": "Kontextüberladung macht jede Runde des Agenten langsamer und teurer.",
    "japanese": "コンテキストの肥大化は、エージェントの処理を遅く高価にする。",
}
old = tiktoken.get_encoding("cl100k_base")
for lang, sentence in samples.items():
    new_count = len(encoding.encode(sentence))
    old_count = len(old.encode(sentence))
    print(f"{lang} o200k_base: {new_count} cl100k_base: {old_count}")
