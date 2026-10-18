"""The Recipe Box data and the one tool the assistant can call."""

RECIPES = {
    "Nani's Lemon Semolina Cake": "Bake at 170 C for 38 minutes.",
    "Tomato Rasam": "Simmer for 15 minutes, add tempering at the end.",
    "Sunday Pancakes": "Rest the batter for 10 minutes before cooking.",
}

DELETE_RECIPE_TOOL = {
    "type": "function",
    "function": {
        "name": "delete_recipe",
        "description": "Delete a recipe from the user's Recipe Box.",
        "parameters": {
            "type": "object",
            "properties": {"name": {"type": "string", "description": "Recipe name"}},
            "required": ["name"],
        },
    },
}


def delete_recipe(name):
    if name in RECIPES:
        del RECIPES[name]
        return f"Deleted '{name}'."
    return f"No recipe named '{name}'."
