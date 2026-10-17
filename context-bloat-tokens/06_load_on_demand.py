"""Keeps a short index of the 20 tools in the request and loads full definitions
only for the tools a search query matches; does the same for four small skills."""
from ctx import tokens, tokens_json
from catalog_tools import TOOLS

SKILLS = {
    "renew-loan": "Walks through renewing a loan, including the hold and limit checks.",
    "overdue-report": "Builds the weekly overdue-loans report grouped by branch.",
    "catalog-import": "Imports a batch of new titles from a publisher's CSV feed.",
    "member-merge": "Merges two member records when a duplicate signup is found.",
}

STOPWORDS = {"a", "an", "the", "for", "of", "to", "and", "or"}


def tool_index() -> list:
    return [{"name": t["name"], "summary": t["description"].split(".")[0] + "."} for t in TOOLS]


def search_tools(query: str) -> list:
    words = {w for w in query.lower().split() if w not in STOPWORDS}
    matches = []
    for t in TOOLS:
        haystack = (t["name"] + " " + t["description"]).lower()
        hits = sum(1 for w in words if w in haystack)
        if hits >= 2:
            matches.append(t)
    return matches


if __name__ == "__main__":
    index = tokens_json(tool_index())
    loaded = search_tools("renew a loan for a member")
    on_demand = index + tokens_json(loaded)
    all_tools = tokens_json(TOOLS)

    skill_front = sum(tokens(f"{n}: {d}") for n, d in SKILLS.items())
    one_skill_body = (
        "Steps: 1. Call get_member to confirm the member has no suspended status. "
        "2. Call list_holds for the loan's book_id; if any hold exists, stop and "
        "report it. 3. Call renew_loan. 4. Report the new due date to the member."
    )
    skills_in_full = skill_front + sum(tokens(one_skill_body) for _ in SKILLS)
    descriptions_plus_one = skill_front + tokens(one_skill_body)

    print("names", [t["name"] for t in loaded])
    print("all tools", all_tools, "index only", index, "index + loaded", on_demand)
    print("skills in full", skills_in_full, "descriptions only", skill_front,
          "descriptions + 1 loaded", descriptions_plus_one)
