"""20 tool definitions of a library catalog MCP server, in the shape of an MCP
tools/list result: each entry has a name, a description and an inputSchema."""


def _tool(name, description, properties, required):
    return {
        "name": name,
        "description": description,
        "inputSchema": {"type": "object", "properties": properties, "required": required},
    }


def _prop(description):
    return {"type": "string", "description": description}


TOOLS = [
    _tool(
        "search_books",
        "Search the catalog for books by title, author or ISBN, and return matching "
        "records with availability.",
        {
            "query": _prop("Search text matched against title, author and ISBN."),
            "limit": _prop("Maximum number of results to return, default 20."),
        },
        ["query"],
    ),
    _tool(
        "get_book",
        "Look up one book by its catalog id and return its full record, including "
        "every copy and its current status.",
        {"book_id": _prop("The catalog id of the book, for example B-04213.")},
        ["book_id"],
    ),
    _tool(
        "get_member",
        "Look up a library member by member id and return their profile, including "
        "membership tier and current hold count.",
        {"member_id": _prop("The member id, for example M-00931.")},
        ["member_id"],
    ),
    _tool(
        "search_members",
        "Search members by name or email and return matching profiles.",
        {"query": _prop("Search text matched against name and email.")},
        ["query"],
    ),
    _tool(
        "create_loan",
        "Create a new loan of one copy of a book to a member, and mark that copy as "
        "checked out.",
        {
            "book_id": _prop("The catalog id of the book to loan."),
            "member_id": _prop("The member id the loan is created for."),
        },
        ["book_id", "member_id"],
    ),
    _tool(
        "renew_loan",
        "Renew an existing loan by its loan id, extending the due date by the "
        "standard renewal period, unless the loan is blocked by a hold or has "
        "reached the renewal limit.",
        {"loan_id": _prop("The id of the loan to renew, for example L-20931.")},
        ["loan_id"],
    ),
    _tool(
        "return_loan",
        "Mark a loan as returned, free the copy for the next member, and clear any "
        "overdue fee that is still open on that loan.",
        {"loan_id": _prop("The id of the loan being returned.")},
        ["loan_id"],
    ),
    _tool(
        "list_loans",
        "List the loans of one member, optionally filtered to only the loans that "
        "are overdue.",
        {
            "member_id": _prop("The member id whose loans to list."),
            "overdue_only": _prop("Set to true to return only loans past their due date."),
        },
        ["member_id"],
    ),
    _tool(
        "place_hold",
        "Place a hold for a member on a book that has no copy currently available, "
        "so the member is notified when a copy is returned.",
        {
            "book_id": _prop("The catalog id of the book to hold."),
            "member_id": _prop("The member id placing the hold."),
        },
        ["book_id", "member_id"],
    ),
    _tool(
        "cancel_hold",
        "Cancel an existing hold by its hold id.",
        {"hold_id": _prop("The id of the hold to cancel.")},
        ["hold_id"],
    ),
    _tool(
        "list_holds",
        "List the active holds on one book, in the order they will be filled.",
        {"book_id": _prop("The catalog id of the book.")},
        ["book_id"],
    ),
    _tool(
        "calculate_fee",
        "Calculate the overdue fee currently owed on a loan, based on how many days "
        "past the due date it is.",
        {"loan_id": _prop("The id of the loan to calculate the fee for.")},
        ["loan_id"],
    ),
    _tool(
        "pay_fee",
        "Record a payment against a member's outstanding fees and reduce their "
        "balance by the amount paid.",
        {
            "member_id": _prop("The member id paying the fee."),
            "amount": _prop("The amount being paid, in the library's local currency."),
        },
        ["member_id", "amount"],
    ),
    _tool(
        "add_book",
        "Add a new title to the catalog with its metadata and an initial number of "
        "copies.",
        {
            "title": _prop("The title of the book."),
            "author": _prop("The author of the book."),
            "isbn": _prop("The ISBN of the book."),
            "copies": _prop("The number of physical copies to add."),
        },
        ["title", "author", "isbn", "copies"],
    ),
    _tool(
        "retire_book",
        "Remove a title from the catalog once every copy has been withdrawn from "
        "circulation.",
        {"book_id": _prop("The catalog id of the book to retire.")},
        ["book_id"],
    ),
    _tool(
        "list_overdue",
        "List every loan in the library that is currently overdue, across all "
        "members.",
        {"branch_id": _prop("Limit the list to one branch, or omit for every branch.")},
        [],
    ),
    _tool(
        "get_branch",
        "Look up one library branch by its id and return its address, hours and the "
        "number of members registered there.",
        {"branch_id": _prop("The id of the branch.")},
        ["branch_id"],
    ),
    _tool(
        "transfer_copy",
        "Transfer one physical copy of a book from one branch to another.",
        {
            "book_id": _prop("The catalog id of the book."),
            "from_branch_id": _prop("The branch the copy is leaving."),
            "to_branch_id": _prop("The branch the copy is going to."),
        },
        ["book_id", "from_branch_id", "to_branch_id"],
    ),
    _tool(
        "get_reading_history",
        "Return the list of books a member has borrowed in the past, most recent "
        "first.",
        {"member_id": _prop("The member id whose history to return.")},
        ["member_id"],
    ),
    _tool(
        "recommend_books",
        "Suggest books for a member based on their reading history and the "
        "catalog's most borrowed titles in the same genre.",
        {"member_id": _prop("The member id to generate recommendations for.")},
        ["member_id"],
    ),
]
