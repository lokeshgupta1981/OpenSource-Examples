Source code for the article [Prompt Injection in LLM Apps: Risks and Defenses](https://howtodoinjava.com/ai/prompt-injection-llm-apps/)

A small Recipe Box assistant in Python that shows a direct and an indirect prompt injection
attack, then the same assistant with five defenses, and a script that counts how often each
attack works.

## Requirements

- Python 3.10 or newer (standard library only, no pip packages)
- Ollama with the `qwen2.5:1.5b` model: `ollama pull qwen2.5:1.5b`

Any OpenAI-compatible API works: set `LLM_BASE_URL`, `LLM_MODEL` and `LLM_API_KEY`.

## Run

```bash
python3 assistant_vulnerable.py direct     # leaks the discount code
python3 assistant_vulnerable.py indirect   # the shared page deletes a recipe
python3 assistant_defended.py page         # summary only, nothing deleted
python3 assistant_defended.py ask Delete the recipe Tomato Rasam.
python3 attack_test.py                     # runs each attack 10 times
python3 -m unittest test_injection.py      # fails if an attack gets through
```

## Files

- `llm.py`: a small client for `/v1/chat/completions`
- `recipe_box.py`: the recipes and the `delete_recipe` tool
- `shared_page.txt`: a recipe page with a hidden instruction
- `assistant_vulnerable.py`: no defenses
- `defenses.py`: `wrap_untrusted`, `tool_call_allowed`, `confirm`
- `assistant_defended.py`: the assistant with all defenses
- `attack_test.py`: counts successful attacks over 10 runs
- `test_injection.py`: unit tests for the defended assistant
