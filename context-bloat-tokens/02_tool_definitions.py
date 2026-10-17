"""Measures the token cost of the 20 library catalog tool definitions."""
import json
from ctx import tokens, tokens_json
from catalog_tools import TOOLS

if __name__ == "__main__":
    total = tokens_json(TOOLS)  # all 20 definitions as compact JSON
    per_tool = {t["name"]: tokens_json(t) for t in TOOLS}
    largest = max(per_tool, key=per_tool.get)
    pretty = tokens(json.dumps(TOOLS, indent=2))
    param_text = sum(
        tokens(p["description"])
        for t in TOOLS
        for p in t["inputSchema"]["properties"].values()
    )

    print("total", total)
    print("average", round(total / len(TOOLS)))
    print("largest", largest, per_tool[largest])
    print("param_text", param_text)
    print("pretty", pretty)
