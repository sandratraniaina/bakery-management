-- Clear existing data (if needed)
TRUNCATE TABLE turnover, sale, loss, breadmaking, ingredient_supply, 
             product, recipe_ingredient, recipe, ingredient, bm_user CASCADE;

-- Reset sequences
ALTER SEQUENCE turnover_id_seq RESTART WITH 1;
ALTER SEQUENCE sale_id_seq RESTART WITH 1;
ALTER SEQUENCE loss_id_seq RESTART WITH 1;
ALTER SEQUENCE breadmaking_id_seq RESTART WITH 1;
ALTER SEQUENCE ingredient_supply_id_seq RESTART WITH 1;
ALTER SEQUENCE product_id_seq RESTART WITH 1;
ALTER SEQUENCE recipe_id_seq RESTART WITH 1;
ALTER SEQUENCE recipe_ingredient_id_seq RESTART WITH 1;
ALTER SEQUENCE ingredient_id_seq RESTART WITH 1;
ALTER SEQUENCE bm_user_id_seq RESTART WITH 1;

-- 1. Create users with different roles
INSERT INTO
    bm_user (username, password_hash, role, created_at)
VALUES
    (
        'aina',
        '$2a$10$WJTut1yB6q6.ql3cUIyxGuhruO/uOhCHadQPeEYq5FrzxVUFv4M6G',
        'ADMIN',
        CURRENT_TIMESTAMP
    );

-- 2. Add ingredients with different units
INSERT INTO ingredient (name, unit, cost_per_unit, stock_quantity, minimum_stock) VALUES
('Wheat Flour', 'KG', 4500, 100.00, 20.00),         -- Premium flour
('Brown Sugar', 'KG', 6000, 50.00, 10.00),          -- Local brown sugar
('Salt', 'KG', 2000, 25.00, 5.00),                  -- Iodized salt
('Active Dry Yeast', 'KG', 25000, 5.00, 1.00),      -- Imported yeast
('Fresh Milk', 'L', 4000, 50.00, 10.00),            -- Local fresh milk
('Purified Water', 'L', 1500, 100.00, 20.00),       -- Filtered water
('Butter', 'KG', 22000, 30.00, 5.00),               -- Local butter
('Vanilla Extract', 'L', 35000, 2.00, 0.50);        -- Premium vanilla

-- 3. Create recipes with ingredients
INSERT INTO recipe (name, description) VALUES
('Pain Maison', 'Traditional Malagasy home-style bread'),
('Vanilla Cookies', 'Sweet vanilla-flavored cookies'),
('Milk Bread', 'Soft and fluffy milk bread');

-- Add ingredients to recipes
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity) VALUES
-- Pain Maison (makes 10 loaves)
(1, 1, 2.50),     -- 2.5kg Wheat Flour
(1, 2, 0.15),     -- 150g Brown Sugar
(1, 3, 0.05),     -- 50g Salt
(1, 4, 0.03),     -- 30g Yeast
(1, 6, 1.50),     -- 1.5L Water

-- Vanilla Cookies (makes 50 cookies)
(2, 1, 1.00),     -- 1kg Wheat Flour
(2, 2, 0.50),     -- 500g Brown Sugar
(2, 3, 0.01),     -- 10g Salt
(2, 7, 0.50),     -- 500g Butter
(2, 8, 0.02),     -- 20ml Vanilla Extract

-- Milk Bread (makes 8 loaves)
(3, 1, 2.00),     -- 2kg Wheat Flour
(3, 2, 0.20),     -- 200g Brown Sugar
(3, 3, 0.04),     -- 40g Salt
(3, 4, 0.025),    -- 25g Yeast
(3, 5, 0.75),     -- 750ml Fresh Milk
(3, 7, 0.30);     -- 300g Butter

-- 4. Create products linked to recipes
INSERT INTO product (recipe_id, name, price, stock_quantity) VALUES
(1, 'Pain Maison', 2500, 0),             -- Traditional bread
(2, 'Vanilla Cookies (10pc)', 6000, 0),  -- Cookie pack
(3, 'Milk Bread', 3500, 0);              -- Premium milk bread