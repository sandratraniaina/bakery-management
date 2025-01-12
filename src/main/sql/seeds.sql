-- Disable all triggers temporarily
SET session_replication_role = 'replica';

-- Clear all tables in the correct order to handle foreign key constraints
-- First, clear junction/child tables
TRUNCATE TABLE sale_details CASCADE;
TRUNCATE TABLE sale CASCADE;
TRUNCATE TABLE product_movement CASCADE;
TRUNCATE TABLE ingredient_movement CASCADE;
TRUNCATE TABLE ingredient_forecast CASCADE;
TRUNCATE TABLE breadmaking CASCADE;
TRUNCATE TABLE recipe_ingredient CASCADE;

-- Then clear main tables
TRUNCATE TABLE product CASCADE;
TRUNCATE TABLE recipe CASCADE;
TRUNCATE TABLE ingredient CASCADE;
TRUNCATE TABLE turnover CASCADE;
TRUNCATE TABLE bm_user CASCADE;

-- Reset all sequences
ALTER SEQUENCE sale_details_id_seq RESTART WITH 1;
ALTER SEQUENCE sale_id_seq RESTART WITH 1;
ALTER SEQUENCE product_movement_id_seq RESTART WITH 1;
ALTER SEQUENCE ingredient_movement_id_seq RESTART WITH 1;
ALTER SEQUENCE ingredient_forecast_id_seq RESTART WITH 1;
ALTER SEQUENCE breadmaking_id_seq RESTART WITH 1;
ALTER SEQUENCE product_id_seq RESTART WITH 1;
ALTER SEQUENCE recipe_id_seq RESTART WITH 1;
ALTER SEQUENCE ingredient_id_seq RESTART WITH 1;
ALTER SEQUENCE turnover_id_seq RESTART WITH 1;
ALTER SEQUENCE bm_user_id_seq RESTART WITH 1;

-- Re-enable triggers
SET session_replication_role = 'origin';

-- 1. Create users with different roles
INSERT INTO
    bm_user (id, username, password_hash, role, created_at)
VALUES
    (   
        1,
        'aina',
        '$2a$10$WJTut1yB6q6.ql3cUIyxGuhruO/uOhCHadQPeEYq5FrzxVUFv4M6G',
        'ADMIN',
        CURRENT_TIMESTAMP
    );

-- Insert Ingredients
INSERT INTO ingredient (name, ingredient_type, unit, cost_per_unit, stock_quantity, minimum_stock) VALUES
('All-Purpose Flour', 'FLOUR', 'KG', 2500.00, 50.00, 10.00),
('Whole Wheat Flour', 'FLOUR', 'KG', 3000.00, 30.00, 8.00),
('Sugar', 'ADD_INS', 'KG', 3000.00, 30.00, 5.00),
('Salt', 'ADD_INS', 'KG', 1500.00, 10.00, 2.00),
('Active Dry Yeast', 'ADD_INS', 'G', 150.00, 1000.00, 200.00),
('Butter', 'BASE', 'KG', 12000.00, 20.00, 5.00),
('Fresh Milk', 'BASE', 'L', 4000.00, 30.00, 5.00),
('Eggs', 'BASE', 'G', 25.00, 5000.00, 1000.00),
('Chocolate Chips', 'ADD_INS', 'G', 35.00, 2000.00, 500.00),
('Vanilla Extract', 'ADD_INS', 'ML', 100.00, 1000.00, 200.00);

-- Insert Recipes
INSERT INTO recipe (name, description) VALUES
('Classic Baguette', 'Traditional French bread with crispy exterior and chewy interior. 4-hour fermentation process required.'),
('Butter Croissant', 'Flaky layers of buttery pastry made over 3 days with traditional French folding technique.'),
('Chocolate Chip Cookies', 'Classic American cookies studded with premium chocolate chips. Crispy edges, chewy center.'),
('Vanilla Pound Cake', 'Rich, buttery cake infused with Madagascar vanilla. Perfect density and moisture.'),
('Japanese Milk Bread', 'Ultra-soft Asian-style bread using tangzhong method. Pillowy texture.'),
('Danish Pastry', 'Light and flaky pastry with 24 delicate layers. Versatile base for various fillings.'),
('Classic Muffins', 'Perfectly domed breakfast muffins. Tender crumb with crispy top.'),
('Chocolate Brownies', 'Fudgy, rich chocolate squares made with premium cocoa. Dense and moist.'),
('French Bread', 'Traditional French bread with scored top. Long fermentation for best flavor.'),
('Butter Cookies', 'Crisp, delicate cookies with rich butter flavor. Perfect with tea or coffee.');

-- Insert Products
INSERT INTO product (recipe_id, name, price, stock_quantity) VALUES
(1, 'Baguette', 2500, 0),
(2, 'Croissant', 3000, 0),
(3, 'Chocolate Chip Cookies (6pc)', 6000, 0),
(4, 'Vanilla Cake', 25000, 0),
(5, 'Milk Bread', 3500, 0),
(6, 'Danish Pastry', 4000, 0),
(7, 'Classic Muffins', 3000, 0),
(8, 'Chocolate Brownies', 4500, 0),
(9, 'French Bread', 3000, 0),
(10, 'Butter Cookies (8pc)', 5000, 0);

-- Insert Recipe Ingredients
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity) VALUES
-- Baguette
(1, 1, 0.500),   -- All-Purpose Flour
(1, 4, 0.010),   -- Salt
(1, 5, 0.007),   -- Yeast

-- Croissant
(2, 1, 0.500),   -- Flour
(2, 6, 0.250),   -- Butter
(2, 7, 0.125),   -- Milk
(2, 5, 0.010),   -- Yeast

-- Chocolate Chip Cookies
(3, 1, 0.250),   -- Flour
(3, 6, 0.150),   -- Butter
(3, 3, 0.150),   -- Sugar
(3, 9, 0.200),   -- Chocolate Chips

-- Vanilla Cake
(4, 1, 0.400),   -- Flour
(4, 6, 0.250),   -- Butter
(4, 3, 0.300),   -- Sugar
(4, 10, 0.015),  -- Vanilla

-- Milk Bread
(5, 1, 0.400),   -- Flour
(5, 7, 0.200),   -- Milk
(5, 6, 0.080),   -- Butter
(5, 5, 0.007),   -- Yeast

-- Danish
(6, 1, 0.400),   -- Flour
(6, 6, 0.250),   -- Butter
(6, 3, 0.050),   -- Sugar
(6, 5, 0.007),   -- Yeast

-- Muffins
(7, 1, 0.300),   -- Flour
(7, 3, 0.150),   -- Sugar
(7, 6, 0.120),   -- Butter
(7, 7, 0.200),   -- Milk

-- Brownies
(8, 1, 0.200),   -- Flour
(8, 9, 0.300),   -- Chocolate
(8, 3, 0.250),   -- Sugar
(8, 6, 0.200),   -- Butter

-- French Bread
(9, 1, 0.500),   -- Flour
(9, 4, 0.010),   -- Salt
(9, 5, 0.007),   -- Yeast

-- Butter Cookies
(10, 1, 0.300),  -- Flour
(10, 6, 0.200),  -- Butter
(10, 3, 0.150),  -- Sugar
(10, 10, 0.005); -- Vanilla