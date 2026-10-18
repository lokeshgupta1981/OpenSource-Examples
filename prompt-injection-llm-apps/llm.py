"""Small client for any OpenAI-compatible chat API (Ollama by default)."""
import json
import os
import urllib.request

BASE_URL = os.environ.get("LLM_BASE_URL", "http://localhost:11434/v1")
MODEL = os.environ.get("LLM_MODEL", "qwen2.5:1.5b")
API_KEY = os.environ.get("LLM_API_KEY", "ollama")


def chat(messages, tools=None, seed=1):
    """Send messages to the model and return the reply message as a dict."""
    body = {"model": MODEL, "messages": messages, "temperature": 0.7, "seed": seed,
            "max_tokens": 300}
    if tools:
        body["tools"] = tools
    request = urllib.request.Request(
        BASE_URL + "/chat/completions",
        data=json.dumps(body).encode("utf-8"),
        headers={"Content-Type": "application/json",
                 "Authorization": "Bearer " + API_KEY},
    )
    with urllib.request.urlopen(request, timeout=300) as response:
        return json.load(response)["choices"][0]["message"]
