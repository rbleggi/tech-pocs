# SQL Query Generation

"I'm going to demonstrate how Claude generates SQL queries and executes them automatically. I've already asked Claude to set up the database environment for us."

"The database is related to an e-commerce platform, with tables for users, orders, products, and categories."

### First Example Basic Query with Aggregations

```
Database: PostgreSQL (latest) running in Docker (container: postgres-sql-demo) (db: testdb)

Task: Get all active users who made a purchase in the last 30 days.
Show their name, email, number of orders, and total spent.
Order by total spent descending.

Please:
1. Generate the SQL query
2. Execute the query in the database
3. Show me the results
```

### Second Example Complex Analytics with Window Functions

```
Database: PostgreSQL (latest) running in Docker (container: postgres-sql-demo) (db: testdb)

Task: For each product category, calculate:
- Total revenue in the last 90 days
- Percentage of total revenue that each category represents
- Rank of each category by revenue
- Month-over-month revenue growth percentage

Include only categories with at least $10,000 in revenue.

Please:
1. Generate the SQL query with CTEs and window functions
2. Execute the query in the database
3. Show me the results
```

### Third Example Performance Optimization

```
Database: PostgreSQL (latest) running in Docker (container: postgres-sql-demo) (db: testdb)

This query runs slowly on a table with 1M+ employees:

SELECT
    d.name as department_name,
    COUNT(DISTINCT e.id) as employee_count,
    AVG(e.salary) as avg_salary,
    COUNT(DISTINCT p.id) as active_projects
FROM employees e
JOIN departments d ON e.department_id = d.id
LEFT JOIN projects p ON d.id = p.department_id
    AND p.end_date >= CURRENT_DATE
WHERE e.hire_date >= '2020-01-01'
GROUP BY d.id, d.name
HAVING COUNT(DISTINCT e.id) > 10
ORDER BY employee_count DESC;

Please:
1. Identify performance bottlenecks
2. Suggest query optimizations
3. Execute EXPLAIN ANALYZE to show the current execution plan
```
