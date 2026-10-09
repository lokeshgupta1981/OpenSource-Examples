import duckdb

# Runs every statement in queries.sql and prints the result of each query
con = duckdb.connect()
statements = [s.strip() for s in open("queries.sql").read().split(";") if s.strip()]
for statement in statements:
    result = con.sql(statement)
    if result is not None:
        print(result.df().to_string(index=False))
        print()
