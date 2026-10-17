Source code for the article https://howtodoinjava.com/ai/context-bloat-in-ai-agents/

Ten Python scripts that measure the token cost of one coding-agent request against a
library catalog MCP server, and apply four techniques that reduce it: trimming tool
output, retrieving chunks instead of pasting files, compacting conversation history,
and loading tool definitions on demand. Two more scripts measure subagents and prompt
caching on their own.

Versions: Python 3.13.16, tiktoken 0.14.0.

Run:

    pip install tiktoken
    python3 09_before_after.py

or run every script in order:

    ./run_all.sh
