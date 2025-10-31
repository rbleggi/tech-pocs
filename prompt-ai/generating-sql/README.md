# AI-Powered SQL Query Generation

A practical guide demonstrating how to leverage AI for SQL query generation, optimization, and debugging by providing database schema context.

## Stack

- Claude code
- Docker

## Quick Start with Claude

Claude can automatically set up the database, load data, and verify the setup with a single prompt:

```
Database: PostgreSQL (latest) running in Docker (container: postgres-sql-demo)

I have two SQL files that need to be executed:
1. schema.sql - creates the database tables
2. seed.sql - populates the tables with sample data

Please:
1. Start a fresh PostgreSQL (latest) container
2. Load schema.sql into the database
3. Load seed.sql into the database
4. Show me all tables created with row counts
```

Claude will automatically:
- Create and start the PostgreSQL Docker container
- Read both SQL files
- Load schema and seed data
- Display all created tables with their row counts

## Demonstration Prompts

### Prompt 1: Basic Query with Aggregations

**Input:**
```
Database: PostgreSQL (latest) running in Docker (container: postgres-sql-demo)

Task: Get all active users who made a purchase in the last 30 days.
Show their name, email, number of orders, and total spent.
Order by total spent descending.

Please:
1. Verify the container is running
2. Generate the SQL query
3. Execute the query in the database
4. Show me the results
```

**Expected Output:**
Claude will:
1. Check if container is running
2. Generate the optimized SQL query
3. Execute it against the database
4. Display results with 8 active users sorted by spending

```
Query Results

  Container Status: postgres-sql-demo is Up 5 minutes

  SQL Query:
  SELECT
      u.name,
      u.email,
      COUNT(o.id) as number_of_orders,
      SUM(o.total) as total_spent
  FROM users u
  INNER JOIN orders o ON u.id = o.user_id
  WHERE u.is_active = true
  AND o.order_date >= NOW() - INTERVAL '30 days'
  GROUP BY u.id, u.name, u.email
  ORDER BY total_spent DESC;

  Results:

  | Name           | Email                      | Number of Orders | Total Spent |
  |----------------|----------------------------|------------------|-------------|
  | Fiona Green    | fiona.green@example.com    | 2                | $575.00     |
  | Jane Smith     | jane.smith@example.com     | 2                | $525.25     |
  | Bob Johnson    | bob.johnson@example.com    | 1                | $500.00     |
  | Ethan Hunt     | ethan.hunt@example.com     | 1                | $450.00     |
  | John Doe       | john.doe@example.com       | 3                | $440.49     |
  | Alice Williams | alice.williams@example.com | 2                | $349.99     |
  | George Wilson  | george.wilson@example.com  | 1                | $199.99     |
  | Diana Prince   | diana.prince@example.com   | 1                | $120.00     |

  Summary:
  - Found 8 active users with purchases in the last 30 days
  - Total orders: 14
  - Combined spending: $3,161.72

  The query correctly filters out inactive users (Charlie Brown and Hannah Lee) and orders older than 30 days.
```

### Prompt 2: Complex Analytics with Window Functions

**Input:**
```
Database: PostgreSQL (latest) running in Docker (container: postgres-sql-demo)

Task: For each product category, calculate:
- Total revenue in the last 90 days
- Percentage of total revenue that each category represents
- Rank of each category by revenue
- Month-over-month revenue growth percentage

Include only categories with at least $10,000 in revenue.

Please:
1. Verify the container is running
2. Generate the SQL query with CTEs and window functions
3. Execute the query in the database
4. Show me the results
```

**Expected Output:**
Claude will:
1. Check if container is running
2. Generate a complex query using CTEs, window functions, and aggregations
3. Execute it against the database
4. Display revenue analysis by product category

```
Query Results

  Container Status: postgres-sql-demo is Up 7 minutes

  SQL Query with CTEs and Window Functions:
  WITH category_revenue_90d AS (
      SELECT
          p.category,
          SUM(s.revenue) as total_revenue
      FROM sales s
      JOIN products p ON s.product_id = p.id
      WHERE s.sale_date >= CURRENT_DATE - INTERVAL '90 days'
      GROUP BY p.category
  ),
  total_revenue AS (
      SELECT SUM(total_revenue) as overall_total
      FROM category_revenue_90d
  ),
  monthly_category_revenue AS (
      SELECT
          p.category,
          DATE_TRUNC('month', s.sale_date) as month,
          SUM(s.revenue) as monthly_revenue
      FROM sales s
      JOIN products p ON s.product_id = p.id
      WHERE s.sale_date >= CURRENT_DATE - INTERVAL '90 days'
      GROUP BY p.category, DATE_TRUNC('month', s.sale_date)
  ),
  mom_growth AS (
      SELECT
          category,
          month,
          monthly_revenue,
          LAG(monthly_revenue) OVER (PARTITION BY category ORDER BY month) as prev_month_revenue,
          ROW_NUMBER() OVER (PARTITION BY category ORDER BY month DESC) as month_rank
      FROM monthly_category_revenue
  ),
  latest_mom_growth AS (
      SELECT
          category,
          CASE
              WHEN prev_month_revenue IS NULL OR prev_month_revenue = 0 THEN NULL
              ELSE ROUND(((monthly_revenue - prev_month_revenue) / prev_month_revenue * 100), 2)
          END as mom_growth_pct
      FROM mom_growth
      WHERE month_rank = 1
  )
  SELECT
      cr.category,
      ROUND(cr.total_revenue, 2) as total_revenue_90d,
      ROUND((cr.total_revenue / tr.overall_total * 100), 2) as pct_of_total,
      RANK() OVER (ORDER BY cr.total_revenue DESC) as revenue_rank,
      COALESCE(mg.mom_growth_pct, 0.00) as mom_growth_pct
  FROM category_revenue_90d cr
  CROSS JOIN total_revenue tr
  LEFT JOIN latest_mom_growth mg ON cr.category = mg.category
  WHERE cr.total_revenue >= 10000
  ORDER BY cr.total_revenue DESC;

  Results (Categories with >= $10,000 revenue):

  | Category    | Total Revenue (90d) | % of Total | Rank | MoM Growth % |
  |-------------|---------------------|------------|------|--------------|
  | Electronics | $25,229.19          | 67.63%     | 1    | 43.06%       |

  All Categories (for context):

  | Category    | Total Revenue (90d) | % of Total | Rank | MoM Growth % |
  |-------------|---------------------|------------|------|--------------|
  | Electronics | $25,229.19          | 67.63%     | 1    | 43.06%       |
  | Furniture   | $8,924.78           | 23.92%     | 2    | 97.50%       |
  | Appliances  | $3,149.65           | 8.44%      | 3    | 33.33%       |

  Monthly Revenue Breakdown:

  | Category    | Sept 2025  | Oct 2025   | Growth  |
  |-------------|------------|------------|---------|
  | Electronics | $10,379.62 | $14,849.57 | +43.06% |
  | Furniture   | $2,999.90  | $5,924.88  | +97.50% |
  | Appliances  | $1,349.85  | $1,799.80  | +33.33% |

  Analysis:
  - Only Electronics meets the $10,000 minimum revenue threshold
  - Electronics dominates with 67.63% of total revenue
  - Furniture shows the strongest growth at 97.50% MoM, but falls short of the $10K threshold
  - All categories show positive month-over-month growth
  - Total 90-day revenue across all categories: $37,303.62
```

### Prompt 3: Performance Optimization

**Input:**
```
Database: PostgreSQL (latest) running in Docker (container: postgres-sql-demo)

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
1. Verify the container is running
2. Identify performance bottlenecks
3. Recommend specific indexes to add
4. Suggest query optimizations
5. Execute EXPLAIN ANALYZE to show the current execution plan
6. Estimate expected performance improvement
```

**Expected Output:**
Claude will:
1. Check if container is running
2. Analyze the query for bottlenecks
3. Recommend composite and partial indexes
4. Provide optimized query version
5. Run EXPLAIN ANALYZE to show execution plan
6. Estimate 10-80x performance improvement with indexes

```
Performance Analysis Report

  Container Status: postgres-sql-demo is Up 10 minutes

  1. Current Execution Plan Analysis

  The EXPLAIN ANALYZE output shows:
  Planning Time: 0.264 ms
  Execution Time: 0.099 ms

  However, with 1M+ employees, this will degrade significantly due to:

  Key Operations:
  - Seq Scan on employees with filter on hire_date (line 20)
  - Hash Join on e.department_id = d.id (line 17-18)
  - Hash Left Join on d.id = p.department_id (line 14-15)
  - Seq Scan on projects with filter on end_date (line 32-33)

  ---
  2. Performance Bottlenecks Identified

  Critical Issues:

  1. Sequential Scan on employees with hire_date filter
    - Currently scanning all 15 rows, filtering down to 13
    - With 1M+ rows: Will scan entire table to find matching hire dates
    - Impact: O(n) complexity on largest table
  2. Missing index on employees.department_id
    - Hash Join requires reading all employee records
    - With 1M+ rows: Expensive hash table construction
    - Impact: Memory pressure and slow join operations
  3. Sequential Scan on projects with end_date filter
    - Scanning all projects to filter by date
    - Impact: Unnecessary full table scans
  4. Missing index on projects.department_id
    - Join requires full table scan
    - Impact: Slower join operations
  5. DISTINCT operations on large result sets
    - COUNT(DISTINCT e.id) and COUNT(DISTINCT p.id)
    - With 1M+ rows: Expensive deduplication
    - Impact: Additional sorting and memory usage

  ---
  3. Recommended Indexes

  Priority 1 - Critical:

  CREATE INDEX idx_employees_hire_date_dept ON employees(hire_date, department_id)
  WHERE hire_date >= '2020-01-01';
  - Type: Partial B-tree index with composite key
  - Purpose: Optimize WHERE clause filter and JOIN simultaneously
  - Benefit: Index-only scan for filtered employees, eliminates full table scan

  Priority 2 - High:

  CREATE INDEX idx_employees_department_id ON employees(department_id);
  - Type: B-tree index
  - Purpose: Accelerate JOIN with departments
  - Benefit: Fast lookup for department relationships

  Priority 3 - High:

  CREATE INDEX idx_projects_dept_enddate ON projects(department_id, end_date)
  WHERE end_date >= CURRENT_DATE;
  - Type: Partial B-tree index with composite key
  - Purpose: Optimize LEFT JOIN and date filter
  - Benefit: Efficient filtering of active projects per department

  Optional - Medium:

  CREATE INDEX idx_employees_id_salary ON employees(id, salary, department_id);
  - Type: Covering index
  - Purpose: Support aggregation calculations
  - Benefit: Avoid table lookups for AVG(salary) computation

  ---
  4. Query Optimizations

  Optimized Query Version:

  WITH filtered_employees AS (
      SELECT
          department_id,
          id,
          salary
      FROM employees
      WHERE hire_date >= '2020-01-01'
  ),
  employee_aggregates AS (
      SELECT
          department_id,
          COUNT(*) as employee_count,
          AVG(salary) as avg_salary
      FROM filtered_employees
      GROUP BY department_id
      HAVING COUNT(*) > 10
  ),
  active_project_counts AS (
      SELECT
          department_id,
          COUNT(DISTINCT id) as active_projects
      FROM projects
      WHERE end_date >= CURRENT_DATE
      GROUP BY department_id
  )
  SELECT
      d.name as department_name,
      ea.employee_count,
      ea.avg_salary,
      COALESCE(apc.active_projects, 0) as active_projects
  FROM employee_aggregates ea
  JOIN departments d ON ea.department_id = d.id
  LEFT JOIN active_project_counts apc ON d.id = apc.department_id
  ORDER BY ea.employee_count DESC;

  Key Improvements:

  1. Early Filtering: CTE filters employees before joins
  2. Eliminated DISTINCT on e.id: Since WHERE on hire_date + GROUP BY department_id naturally produces unique employees
  3. Pre-aggregation: Count and average computed before final join
  4. Reduced Join Complexity: Smaller result sets in CTEs
  5. COALESCE for NULL handling: Cleaner than COUNT with LEFT JOIN

  ---
  5. Expected Performance Improvements

  With Current Data (15 employees):
  - Minimal impact due to small dataset
  - Current execution: ~0.1ms

  With 1M+ Employees (Projected):

  | Metric         | Without Indexes      | With Indexes           | Improvement      |
  |----------------|----------------------|------------------------|------------------|
  | Table Scans    | Full scan (1M rows)  | Index scan (~50K rows) | 95% reduction    |
  | Join Cost      | Hash join on 1M rows | Index nested loop      | 90% reduction    |
  | Memory Usage   | ~80MB hash tables    | ~8MB index buffers     | 90% reduction    |
  | Execution Time | ~15-30 seconds       | ~200-500ms             | 98% reduction    |
  | Planning Time  | ~1-2ms               | ~2-3ms                 | Minimal increase |

  Breakdown by Optimization:

  1. idx_employees_hire_date_dept:
    - Reduces employee scan from 1M to ~500K rows (50% of data hired after 2020)
    - Estimated improvement: 50-60% faster
  2. idx_employees_department_id:
    - Converts hash join to index nested loop join
    - Estimated improvement: 30-40% faster
  3. idx_projects_dept_enddate:
    - Eliminates project table scan
    - Estimated improvement: 10-15% faster
  4. Query rewrite with CTEs:
    - Reduces intermediate result set size
    - Estimated improvement: 20-30% faster

  Combined Effect: 95-98% reduction in execution time

  Disk Space Cost:
  - idx_employees_hire_date_dept: ~20-30MB
  - idx_employees_department_id: ~15-20MB
  - idx_projects_dept_enddate: ~500KB
  - Total: ~35-50MB (0.03% of 1M row table)

  ---
  6. Implementation Steps

  CREATE INDEX CONCURRENTLY idx_employees_hire_date_dept
  ON employees(hire_date, department_id)
  WHERE hire_date >= '2020-01-01';

  CREATE INDEX CONCURRENTLY idx_employees_department_id
  ON employees(department_id);

  CREATE INDEX CONCURRENTLY idx_projects_dept_enddate
  ON projects(department_id, end_date)
  WHERE end_date >= CURRENT_DATE;

  ANALYZE employees;
  ANALYZE projects;
  ANALYZE departments;

  Use CONCURRENTLY to avoid locking tables during index creation on production databases.

  ---
  7. Monitoring Recommendations

  After implementing indexes:

  SELECT
      schemaname,
      tablename,
      indexname,
      idx_scan,
      idx_tup_read,
      idx_tup_fetch
  FROM pg_stat_user_indexes
  WHERE tablename IN ('employees', 'projects')
  ORDER BY idx_scan DESC;

  This will show index usage statistics to validate effectiveness.
```

## Tips for Better Results

- Be specific about requirements
- Mention SQL dialect features if needed
- State performance requirements
- Ask for explanations alongside queries
- Test in development first
