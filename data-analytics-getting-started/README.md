Source code for the article https://howtodoinjava.com/python/what-is-data-analytics/

A first data analysis on a small bakery sales file, once with SQL (DuckDB) and once with Python (pandas).

Versions: Python 3.13, pandas 3.0.6, DuckDB 1.5.6

Run:

    python -m venv venv
    venv/bin/pip install -r requirements.txt     (Windows: venv\Scripts\pip install -r requirements.txt)
    venv/bin/python run_sql.py
    venv/bin/python analysis.py

Files:

- bakery_sales.csv: 14 orders, including one duplicated row (order 7) and one row without a quantity (order 10)
- queries.sql: cleaning, revenue per product and revenue per month in SQL
- run_sql.py: runs queries.sql with DuckDB
- analysis.py: the same steps with pandas
