# AI-Powered SQL Query Generation

A practical guide demonstrating how to leverage AI for SQL query generation, optimization, and debugging by providing database schema context.

## Stack

- PostgreSQL (latest)
- Docker

## Setup

Start PostgreSQL with Docker:

```bash
docker run --name postgres-sql-demo -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=testdb -p 5432:5432 -d postgres:latest
```

Connect to PostgreSQL:

```bash
docker exec -it postgres-sql-demo psql -U postgres -d testdb
```

Stop container:

```bash
docker stop postgres-sql-demo
```

Remove container:

```bash
docker rm postgres-sql-demo
```

## Initialize Database

Load the sample schema:

**Linux/Mac:**
```bash
docker exec -i postgres-sql-demo psql -U postgres -d testdb < schema.sql
```
```bash
docker exec -i postgres-sql-demo psql -U postgres -d testdb < seed.sql
```

### Prompt 1: Basic Query with Aggregations

**Input:**
```
Database: PostgreSQL
Schema: schema.sql

Task: Get all active users who made a purchase in the last 30 days.
Show their name, email, number of orders, and total spent.
Order by total spent descending.
```

**Output:**
```
Based on the schema, here's the SQL query that meets your requirements:

  SELECT
      u.name,
      u.email,
      COUNT(o.id) AS number_of_orders,
      SUM(o.total) AS total_spent
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id
  WHERE u.is_active = true
      AND o.order_date >= NOW() - INTERVAL '30 days'
  GROUP BY u.id, u.name, u.email
  ORDER BY total_spent DESC;

  The query:
  - Joins users and orders tables on schema.sql:25
  - Filters for active users (is_active = true) from schema.sql:19
  - Filters orders from the last 30 days using order_date from schema.sql:27
  - Aggregates the count of orders and sum of totals per user
  - Orders results by total spent in descending order
```

### Prompt 2: Complex Analytics with Window Functions

**Input:**
```
Database: PostgreSQL
Schema: schema.sql

Task: For each product category, calculate:
- Total revenue in the last 90 days
- Percentage of total revenue that each category represents
- Rank of each category by revenue
- Month-over-month revenue growth percentage

Include only categories with at least $10,000 in revenue.
```

**Output:**
```
I'll create a SQL query that calculates category-level metrics including revenue, percentage share, ranking, and month-over-month growth.

  WITH category_revenue_90days AS (
      SELECT
          p.category,
          SUM(s.revenue) AS total_revenue
      FROM products p
      INNER JOIN sales s ON p.id = s.product_id
      WHERE s.sale_date >= CURRENT_DATE - INTERVAL '90 days'
      GROUP BY p.category
      HAVING SUM(s.revenue) >= 10000
  ),
  total_revenue AS (
      SELECT SUM(total_revenue) AS overall_total
      FROM category_revenue_90days
  ),
  monthly_revenue AS (
      SELECT
          p.category,
          SUM(CASE
              WHEN s.sale_date >= CURRENT_DATE - INTERVAL '30 days'
              THEN s.revenue
              ELSE 0
          END) AS current_month_revenue,
          SUM(CASE
              WHEN s.sale_date >= CURRENT_DATE - INTERVAL '60 days'
                  AND s.sale_date < CURRENT_DATE - INTERVAL '30 days'
              THEN s.revenue
              ELSE 0
          END) AS previous_month_revenue
      FROM products p
      INNER JOIN sales s ON p.id = s.product_id
      WHERE s.sale_date >= CURRENT_DATE - INTERVAL '90 days'
      GROUP BY p.category
  )
  SELECT
      cr.category,
      cr.total_revenue,
      ROUND((cr.total_revenue / tr.overall_total * 100), 2) AS revenue_percentage,
      RANK() OVER (ORDER BY cr.total_revenue DESC) AS revenue_rank,
      CASE
          WHEN mr.previous_month_revenue > 0
          THEN ROUND(((mr.current_month_revenue - mr.previous_month_revenue) / mr.previous_month_revenue * 100), 2)
          ELSE NULL
      END AS mom_growth_percentage
  FROM category_revenue_90days cr
  CROSS JOIN total_revenue tr
  INNER JOIN monthly_revenue mr ON cr.category = mr.category
  ORDER BY cr.total_revenue DESC;

  The query:
  - Uses products (schema.sql:30-36) and sales (schema.sql:38-45) tables
  - Filters sales from the last 90 days using sale_date (schema.sql:43)
  - Groups by product category (schema.sql:34)
  - Calculates total revenue per category and filters those >= $10,000
  - Computes percentage of overall revenue for each category
  - Ranks categories using RANK() window function
  - Calculates month-over-month growth by comparing last 30 days vs previous 30 days (30-60 days ago)
  - Returns NULL for growth percentage when previous month revenue is zero to avoid division by zero
```

### Prompt 3: Performance Optimization

**Input:**
```
Database: PostgreSQL
Schema: schema.sql

Current indexes:
- employees: PRIMARY KEY on id
- departments: PRIMARY KEY on id
- projects: PRIMARY KEY on id

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

Tasks:
1. Identify performance bottlenecks
2. Recommend specific indexes to add
3. Suggest query optimizations if applicable
4. Estimate expected performance improvement
```

**Output:**
```
Performance Analysis

  Identified Bottlenecks

  1. Missing foreign key indexes - Joins on employees.department_id (schema.sql:58) and projects.department_id (schema.sql:66) lack
  indexes
  2. Unindexed filter column - WHERE clause filters on employees.hire_date (schema.sql:60) without an index
  3. Unindexed date filter - LEFT JOIN condition on projects.end_date (schema.sql:69) without an index
  4. Sequential scan - With 1M+ employees, full table scan on hire_date is expensive

  Recommended Indexes

  CREATE INDEX idx_employees_department_id ON employees(department_id);

  CREATE INDEX idx_employees_hire_date ON employees(hire_date);

  CREATE INDEX idx_employees_dept_hire ON employees(department_id, hire_date);

  CREATE INDEX idx_projects_department_id ON projects(department_id);

  CREATE INDEX idx_projects_end_date ON projects(end_date) WHERE end_date >= CURRENT_DATE;

  Index Priority:
  1. idx_employees_dept_hire - Composite index (highest impact)
  2. idx_employees_department_id - Foreign key index
  3. idx_projects_department_id - Foreign key index
  4. idx_projects_end_date - Partial index for active projects
  5. idx_employees_hire_date - Single column index (optional if composite exists)

  Query Optimization

  SELECT
      d.name as department_name,
      COUNT(e.id) as employee_count,
      AVG(e.salary) as avg_salary,
      COUNT(DISTINCT p.id) as active_projects
  FROM employees e
  JOIN departments d ON e.department_id = d.id
  LEFT JOIN projects p ON d.id = p.department_id
      AND p.end_date >= CURRENT_DATE
  WHERE e.hire_date >= '2020-01-01'
  GROUP BY d.id, d.name
  HAVING COUNT(e.id) > 10
  ORDER BY employee_count DESC;

  Changes:
  - Changed COUNT(DISTINCT e.id) to COUNT(e.id) since e.id is already unique per row after the JOIN
  - This eliminates unnecessary DISTINCT operations

  Expected Performance Improvements

  Before indexes:
  - Full table scan on 1M+ employees: ~5-10 seconds
  - Nested loop joins without indexes: ~10-30 seconds
  - Total estimated time: 15-40 seconds

  After indexes:
  - Index scan on employees(department_id, hire_date): ~100-500ms
  - Index-based joins: ~200-800ms
  - Total estimated time: 500ms-2 seconds

  Expected improvement: 10-80x faster (95-98% reduction in execution time)

  Verification Commands

  EXPLAIN ANALYZE
  SELECT
      d.name as department_name,
      COUNT(e.id) as employee_count,
      AVG(e.salary) as avg_salary,
      COUNT(DISTINCT p.id) as active_projects
  FROM employees e
  JOIN departments d ON e.department_id = d.id
  LEFT JOIN projects p ON d.id = p.department_id
      AND p.end_date >= CURRENT_DATE
  WHERE e.hire_date >= '2020-01-01'
  GROUP BY d.id, d.name
  HAVING COUNT(e.id) > 10
  ORDER BY employee_count DESC;

  Run this before and after adding indexes to confirm improvements in:
  - Execution time
  - Rows scanned
  - Join method (should change from Seq Scan to Index Scan)
```

## Best Practices

### Effective Prompt Structure

```
Database: [Type and Version]

Schema: [schema.sql file]
[CREATE TABLE statements with relationships]

Current Indexes: [if relevant]
[List of existing indexes]

Task:
[Clear, specific description]
```
Claude will read the `schema.sql` file automatically and use it as context for the queries.

### Always Include

1. Complete schema with data types
2. Foreign key relationships
3. Database type and version
4. Existing indexes for optimization queries
5. Sample data when helpful

### Common Use Cases

**Query Generation:**
- Complex JOINs and aggregations
- Window functions and CTEs
- Date/time calculations
- JSON/Array operations

**Optimization:**
- Index recommendations
- Query rewriting
- Execution plan analysis

**Debugging:**
- Syntax errors
- Logic errors
- Missing relationships

## Tips for Better Results

- Be specific about requirements
- Mention SQL dialect features if needed
- State performance requirements
- Ask for explanations alongside queries
- Test in development first
