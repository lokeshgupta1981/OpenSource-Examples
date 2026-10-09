import pandas as pd

# 1. Load the CSV file
sales = pd.read_csv("bakery_sales.csv", parse_dates=["order_date"])
print("Rows loaded:", len(sales))                      # 14

# 2. Clean the data
sales = sales.drop_duplicates()                         # 13 rows
sales = sales.dropna(subset=["quantity"])               # 12 rows
print("Rows after cleaning:", len(sales))

# 3. Add a revenue column
sales["revenue"] = sales["quantity"] * sales["unit_price"]

# 4. Revenue per product
by_product = sales.groupby("product")["revenue"].sum().sort_values(ascending=False)
print(by_product)                                       # cake 140.0, bread 75.0, cookies 36.0

# 5. Revenue per month
by_month = sales.groupby(sales["order_date"].dt.strftime("%Y-%m"))["revenue"].sum()
print(by_month)                                         # 2026-01 96.0, 2026-02 44.0, 2026-03 111.0
