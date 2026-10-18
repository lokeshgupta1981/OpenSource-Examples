"""Recipe Box assistant with layered defenses against prompt injection."""
import json
import sys

from defenses import confirm, tool_call_allowed, wrap_untrusted
from llm import chat
from recipe_box import DELETE_RECIPE_TOOL, RECIPES, delete_recipe

# Defense 1: no secret in the prompt, so there is nothing to leak.
SYSTEM_PROMPT = ("You are the Recipe Box assistant. "
                 "Help the user with their saved recipes.")


def summarize_page(page_text, seed=1):
    """Defense 2 and 3: mark the page as data, and offer no tools at all."""
    request = ("Summarize the recipe on this page: its ingredients and method.\n\n"
               + wrap_untrusted(page_text))
    messages = [{"role": "system", "content": SYSTEM_PROMPT},
                {"role": "user", "content": request}]
    return chat(messages, seed=seed).get("content") or ""


def ask(user_message, seed=1, ask_human=confirm):
    """Answer the user's own request. Tool calls are checked, then confirmed."""
    messages = [{"role": "system", "content": SYSTEM_PROMPT},
                {"role": "user", "content": user_message}]
    reply = chat(messages, tools=[DELETE_RECIPE_TOOL], seed=seed)
    for call in reply.get("tool_calls") or []:
        name = call["function"]["name"]
        args = json.loads(call["function"]["arguments"])
        # Defense 4: check the tool call in code before running it.
        if not tool_call_allowed(name, args, user_message):
            print("BLOCKED:", name, args)
            continue
        # Defense 5: a person approves every action that changes data.
        if ask_human(f"Delete the recipe '{args['name']}'?"):
            print(delete_recipe(args["name"]))
        else:
            print("Cancelled.")
    return reply.get("content") or ""


if __name__ == "__main__":
    if sys.argv[1] == "page":
        print(summarize_page(open("shared_page.txt", encoding="utf-8").read()))
    else:
        print(ask(" ".join(sys.argv[2:])))
    print("Recipes left:", list(RECIPES))
