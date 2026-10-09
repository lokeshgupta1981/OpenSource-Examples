-- 1. Clean the data: remove the duplicated row and the row without a quantity
CREATE TABLE sales AS
SELECT DISTINCT *
FROM 'bakery_sales.csv'
WHERE quantity IS NOT NULL;

-- 2. Revenue per product
SELECT product,
       SUM(quantity * unit_price) AS revenue
FROM sales
GROUP BY product
ORDER BY revenue DESC;

-- 3. Revenue per month
SELECT strftime(order_date, '%Y-%m') AS month,
       SUM(quantity * unit_price) AS revenue
FROM sales
GROUP BY month
ORDER BY month;
