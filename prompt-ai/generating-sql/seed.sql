-- Sample data for AI SQL generation demonstrations

-- Insert users
INSERT INTO users (name, email, created_at, is_active) VALUES
('John Doe', 'john.doe@example.com', NOW() - INTERVAL '60 days', true),
('Jane Smith', 'jane.smith@example.com', NOW() - INTERVAL '45 days', true),
('Bob Johnson', 'bob.johnson@example.com', NOW() - INTERVAL '90 days', true),
('Alice Williams', 'alice.williams@example.com', NOW() - INTERVAL '30 days', true),
('Charlie Brown', 'charlie.brown@example.com', NOW() - INTERVAL '120 days', false),
('Diana Prince', 'diana.prince@example.com', NOW() - INTERVAL '15 days', true),
('Ethan Hunt', 'ethan.hunt@example.com', NOW() - INTERVAL '75 days', true),
('Fiona Green', 'fiona.green@example.com', NOW() - INTERVAL '20 days', true);

-- Insert orders
INSERT INTO orders (user_id, total, order_date) VALUES
(1, 150.00, NOW() - INTERVAL '25 days'),
(1, 200.50, NOW() - INTERVAL '15 days'),
(1, 89.99, NOW() - INTERVAL '5 days'),
(2, 350.00, NOW() - INTERVAL '20 days'),
(2, 175.25, NOW() - INTERVAL '10 days'),
(3, 500.00, NOW() - INTERVAL '8 days'),
(4, 99.99, NOW() - INTERVAL '3 days'),
(4, 250.00, NOW() - INTERVAL '12 days'),
(5, 1000.00, NOW() - INTERVAL '100 days'),
(6, 120.00, NOW() - INTERVAL '7 days'),
(7, 450.00, NOW() - INTERVAL '18 days'),
(8, 300.00, NOW() - INTERVAL '2 days');

-- Insert products
INSERT INTO products (name, category, price) VALUES
('Laptop Pro', 'Electronics', 1200.00),
('Wireless Mouse', 'Electronics', 25.99),
('Office Chair', 'Furniture', 299.99),
('Standing Desk', 'Furniture', 599.99),
('Coffee Maker', 'Appliances', 89.99),
('Headphones', 'Electronics', 149.99),
('Table Lamp', 'Furniture', 45.00),
('Keyboard', 'Electronics', 79.99),
('Monitor', 'Electronics', 399.99),
('Desk Organizer', 'Office', 19.99);

-- Insert sales
INSERT INTO sales (product_id, quantity, sale_date, revenue) VALUES
(1, 5, CURRENT_DATE - INTERVAL '10 days', 6000.00),
(1, 3, CURRENT_DATE - INTERVAL '20 days', 3600.00),
(2, 15, CURRENT_DATE - INTERVAL '15 days', 389.85),
(3, 8, CURRENT_DATE - INTERVAL '25 days', 2399.92),
(4, 4, CURRENT_DATE - INTERVAL '30 days', 2399.96),
(5, 20, CURRENT_DATE - INTERVAL '5 days', 1799.80),
(6, 10, CURRENT_DATE - INTERVAL '12 days', 1499.90),
(7, 25, CURRENT_DATE - INTERVAL '18 days', 1125.00),
(8, 12, CURRENT_DATE - INTERVAL '22 days', 959.88),
(9, 6, CURRENT_DATE - INTERVAL '8 days', 2399.94),
(1, 7, CURRENT_DATE - INTERVAL '35 days', 8400.00),
(2, 30, CURRENT_DATE - INTERVAL '40 days', 779.70),
(3, 10, CURRENT_DATE - INTERVAL '45 days', 2999.90),
(5, 15, CURRENT_DATE - INTERVAL '50 days', 1349.85),
(6, 8, CURRENT_DATE - INTERVAL '55 days', 1199.92);

-- Insert departments
INSERT INTO departments (name, location) VALUES
('Engineering', 'San Francisco'),
('Sales', 'New York'),
('Marketing', 'Los Angeles'),
('HR', 'Chicago'),
('Finance', 'Boston');

-- Insert employees
INSERT INTO employees (name, department_id, salary, hire_date) VALUES
('Michael Scott', 2, 95000.00, '2020-03-15'),
('Dwight Schrute', 2, 75000.00, '2020-06-01'),
('Jim Halpert', 2, 72000.00, '2021-01-10'),
('Pam Beesly', 3, 68000.00, '2020-09-20'),
('Ryan Howard', 3, 55000.00, '2022-02-01'),
('Angela Martin', 5, 82000.00, '2020-05-12'),
('Kevin Malone', 5, 60000.00, '2020-08-30'),
('Oscar Martinez', 5, 85000.00, '2020-04-15'),
('Stanley Hudson', 2, 78000.00, '2019-11-20'),
('Phyllis Vance', 2, 70000.00, '2020-07-08'),
('Toby Flenderson', 4, 65000.00, '2020-10-01'),
('Kelly Kapoor', 3, 58000.00, '2021-03-15'),
('Creed Bratton', 1, 62000.00, '2019-12-01'),
('Meredith Palmer', 2, 63000.00, '2020-02-14'),
('Darryl Philbin', 1, 88000.00, '2020-01-20');

-- Insert projects
INSERT INTO projects (name, department_id, start_date, end_date, budget) VALUES
('Website Redesign', 1, '2024-01-01', '2024-12-31', 150000.00),
('Customer Portal', 1, '2024-03-01', '2025-03-01', 200000.00),
('Marketing Campaign Q1', 3, '2024-01-01', '2024-03-31', 50000.00),
('Sales Training Program', 2, '2024-02-01', '2024-06-30', 75000.00),
('HR System Upgrade', 4, '2023-10-01', '2024-02-28', 100000.00),
('Financial Audit', 5, '2024-01-15', '2024-12-15', 120000.00),
('Mobile App Development', 1, '2024-04-01', '2025-06-01', 300000.00);

-- Insert order items
INSERT INTO order_items (order_id, product_name, quantity, price) VALUES
(1, 'Laptop Pro', 1, 1200.00),
(2, 'Wireless Mouse', 2, 25.99),
(3, 'Office Chair', 1, 299.99),
(4, 'Standing Desk', 1, 599.99),
(5, 'Coffee Maker', 2, 89.99),
(6, 'Headphones', 3, 149.99),
(7, 'Table Lamp', 1, 45.00),
(8, 'Keyboard', 2, 79.99),
(9, 'Monitor', 2, 399.99),
(10, 'Desk Organizer', 5, 19.99),
(11, 'Laptop Pro', 1, 1200.00),
(12, 'Wireless Mouse', 3, 25.99);
