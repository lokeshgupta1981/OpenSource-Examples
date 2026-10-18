Source code for the article [Fine-Tuning vs RAG vs Prompt Engineering for LLM Apps](https://howtodoinjava.com/ai/fine-tuning-vs-rag-vs-prompt-engineering/)

The Recipe Box assistant, used in the article to compare prompt engineering, RAG and
fine-tuning.

- `RecipeBoxAssistant` sends the same question to an LLM twice through the JDK `HttpClient`:
  once as a plain prompt, once with a system prompt and few-shot examples. It works with any
  OpenAI-compatible chat completions endpoint (Ollama, OpenAI, Azure OpenAI, vLLM).
- `fine-tuning/recipe-box-training.jsonl` is a 10-record training file in the chat
  fine-tuning format (one JSON object per line). `fine-tuning/sample-record.json` is one
  of those records, pretty-printed. No training job is run.

Versions: Java 17 or newer (tested on Java 25), Maven 3.9, Jackson 2.22.4,
Ollama 0.40.6 with `qwen2.5:0.5b`.

Run against a local Ollama:

    ollama pull qwen2.5:0.5b
    mvn compile exec:java

Environment variables (all optional):

| Variable | Default | Meaning |
|---|---|---|
| LLM_BASE_URL | http://localhost:11434/v1 | Base URL of the OpenAI-compatible API |
| LLM_MODEL | qwen2.5:0.5b | Model name |
| LLM_API_KEY | (empty) | Sent as a Bearer token when set, for example for OpenAI |

To run against OpenAI instead, set `LLM_BASE_URL=https://api.openai.com/v1`,
`LLM_MODEL=gpt-4.1-mini` and `LLM_API_KEY` to your key.
