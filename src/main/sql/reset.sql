-- Disable foreign key checks temporarily
SET session_replication_role = 'replica';

-- Truncate all tables
TRUNCATE TABLE 
    sale_product,
    sale,
    turnover,
    loss,
    ingredient_forecast,
    ingredient_supply,
    breadmaking,
    recipe_ingredient,
    product,
    recipe,
    ingredient,
    bm_user
RESTART IDENTITY CASCADE;

-- Enable foreign key checks back
SET session_replication_role = 'origin';
