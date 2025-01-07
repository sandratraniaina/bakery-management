INSERT INTO
    bm_user (username, password_hash, role, created_at)
VALUES
    (
        'aina',
        '$2a$10$WJTut1yB6q6.ql3cUIyxGuhruO/uOhCHadQPeEYq5FrzxVUFv4M6G',
        'Admin',
        CURRENT_TIMESTAMP
    );

-- Insert some recipes
INSERT INTO
    recipe (name, description)
VALUES
    (
        'Basic Bread',
        'A simple bread recipe with flour, water, yeast, and salt.'
    ),
    (
        'Sweet Bread',
        'Bread recipe with added sugar and milk.'
    );

-- Insert some ingredients
INSERT INTO
    ingredient (
        name,
        unit,
        cost_per_unit,
        stock_quantity,
        minimum_stock
    )
VALUES
    ('Flour', 'KG', 2.00, 50.00, 10.00),
    ('Water', 'L', 0.10, 100.00, 20.00),
    ('Yeast', 'G', 0.05, 500.00, 50.00),
    ('Salt', 'G', 0.02, 200.00, 20.00),
    ('Sugar', 'G', 0.03, 300.00, 30.00),
    ('Milk', 'L', 1.50, 20.00, 5.00);

-- Insert ingredients into recipe_ingredient table
INSERT INTO
    recipe_ingredient (recipe_id, ingredient_id, quantity)
VALUES
    (1, 1, 1.00),
    -- 1 kg of Flour
    (1, 2, 0.50),
    -- 0.5 l of Water
    (1, 3, 10.00),
    -- 10 g of Yeast
    (1, 4, 5.00),
    -- 5 g of Salt
    (2, 1, 1.00),
    -- 1 kg of Flour
    (2, 2, 0.50),
    -- 0.5 l of Water
    (2, 3, 10.00),
    -- 10 g of Yeast
    (2, 5, 20.00),
    -- 20 g of Sugar
    (2, 6, 0.25);

-- 0.25 l of Milk
-- Insert some products
INSERT INTO
    product (recipe_id, name, price, stock_quantity)
VALUES
    (1, 'Basic Bread Loaf', 5.00, 100),
    (2, 'Sweet Bread Loaf', 6.50, 50);

-- Insert breadmaking data
INSERT INTO
    breadmaking (
        product_id,
        quantity,
        production_date,
        ingredient_cost,
        other_cost
    )
VALUES
    (1, 50, '2025-01-01', 75.00, 10.00),
    (2, 20, '2025-01-02', 40.00, 5.00);

-- Insert some sales
INSERT INTO
    sale (client_name, total_price, sale_date)
VALUES
    ('John Doe', 150.00, '2025-01-03'),
    ('Jane Smith', 130.00, '2025-01-04');

-- Insert products into sale_product
INSERT INTO
    sale_product (id, product_id, sale_id, quantity, unit_price)
VALUES
    (1, 1, 1, 20, 5.00),
    (2, 2, 2, 10, 6.50);

-- Insert ingredient forecasts
INSERT INTO
    ingredient_forecast (ingredient_id, forecast_date, forecast_quantity)
VALUES
    (1, '2025-02-01', 30.00),
    (2, '2025-02-01', 50.00);

-- Insert ingredient supplies
INSERT INTO
    ingredient_supply (
        ingredient_id,
        quantity,
        supply_date,
        cost_per_unit
    )
VALUES
    (1, 20.00, '2025-01-05', 2.00),
    (2, 40.00, '2025-01-05', 0.10);

-- Insert losses
INSERT INTO
    loss (
        loss_type,
        reference_id,
        reference_name,
        reported_by,
        quantity,
        reason,
        loss_date
    )
VALUES
    (
        'Ingredient',
        1,
        'Flour',
        1,
        5.00,
        'Spillage during storage',
        '2025-01-06'
    ),
    (
        'Product',
        1,
        'Basic Bread Loaf',
        1,
        2.00,
        'Damaged during delivery',
        '2025-01-07'
    );

-- Insert turnovers
INSERT INTO
    turnover (
        turnover_type,
        amount,
        description,
        turnover_date
    )
VALUES
    (
        'Income',
        150.00,
        'Sale to John Doe',
        '2025-01-03'
    ),
    (
        'Expense',
        85.00,
        'Ingredient purchase',
        '2025-01-05'
    );