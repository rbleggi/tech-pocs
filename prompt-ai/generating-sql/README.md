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

## Demonstration Prompts

When using these prompts with Claude, reference the schema file:

```
Database: PostgreSQL
Schema: schema.sql

Task: [Your task from the prompts below]
```

Claude will read the `schema.sql` file automatically and use it as context for the queries.

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
```sql
SELECT
    u.name,
    u.email,
    COUNT(o.id) as order_count,
    SUM(o.total) as total_spent
FROM users u
JOIN orders o ON u.id = o.user_id
WHERE u.is_active = true
  AND o.order_date >= NOW() - INTERVAL '30 days'
GROUP BY u.id, u.name, u.email
ORDER BY total_spent DESC;
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
```sql
WITH category_revenue AS (
    SELECT
        p.category,
        SUM(s.revenue) as total_revenue,
        DATE_TRUNC('month', s.sale_date) as month
    FROM products p
    JOIN sales s ON p.id = s.product_id
    WHERE s.sale_date >= CURRENT_DATE - INTERVAL '90 days'
    GROUP BY p.category, DATE_TRUNC('month', s.sale_date)
),
monthly_growth AS (
    SELECT
        category,
        total_revenue,
        LAG(total_revenue) OVER (PARTITION BY category ORDER BY month) as prev_month_revenue,
        RANK() OVER (ORDER BY total_revenue DESC) as revenue_rank
    FROM category_revenue
)
SELECT
    category,
    total_revenue,
    ROUND(100.0 * total_revenue / SUM(total_revenue) OVER (), 2) as revenue_percentage,
    revenue_rank,
    ROUND(100.0 * (total_revenue - prev_month_revenue) / NULLIF(prev_month_revenue, 0), 2) as mom_growth
FROM monthly_growth
WHERE total_revenue >= 10000
ORDER BY revenue_rank;
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
Performance Bottlenecks:
1. Full table scan on employees (1M+ rows) filtering by hire_date
2. JOIN on department_id without index
3. LEFT JOIN on projects filtering by end_date without index

Recommended Indexes:
CREATE INDEX idx_employees_hire_date ON employees(hire_date);
CREATE INDEX idx_employees_department_id ON employees(department_id);
CREATE INDEX idx_projects_department_end_date ON projects(department_id, end_date);

Composite index for optimal performance:
CREATE INDEX idx_employees_hire_dept ON employees(hire_date, department_id) INCLUDE (salary);

Query Optimization:
- Consider using a CTE to pre-filter employees
- The DISTINCT in COUNT may be unnecessary if relationships are 1:many

Expected Improvement:
- Current: Full table scan (~30-60 seconds)
- With indexes: Index scan (~1-3 seconds)
- Estimated speedup: 10-30x
```

## Best Practices

### Effective Prompt Structure

```
Database: [Type and Version]

Schema:
[CREATE TABLE statements with relationships]

Current Indexes: [if relevant]
[List of existing indexes]

Task:
[Clear, specific description]
```

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
