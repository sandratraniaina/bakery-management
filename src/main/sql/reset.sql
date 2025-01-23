-- Drop all triggers first to avoid dependency issues
DROP TRIGGER IF EXISTS trg_update_ingredient_stock_on_movement ON ingredient_movement;
DROP TRIGGER IF EXISTS trg_validate_production_stock ON breadmaking;
DROP TRIGGER IF EXISTS trg_handle_ingredient_movements_on_production ON breadmaking;
DROP TRIGGER IF EXISTS tgr_update_product_stock_on_movement ON product_movement;
DROP TRIGGER IF EXISTS trg_handle_product_movements_on_production ON breadmaking;
DROP TRIGGER IF EXISTS trg_handle_sale_details_changes ON sale_details;
DROP TRIGGER IF EXISTS trg_create_turnover_on_product_movement ON product_movement;
DROP TRIGGER IF EXISTS trigger_create_turnover_on_ingredient_supply ON ingredient_movement;

-- Drop all views
DROP VIEW IF EXISTS v_daily_ingredient_usage;
DROP VIEW IF EXISTS v_weekly_ingredient_usage;
DROP VIEW IF EXISTS v_yearly_ingredient_usage;
DROP VIEW IF EXISTS v_ingredients_to_reorder;
DROP VIEW IF EXISTS v_breadmaking_ingredients;
DROP VIEW IF EXISTS v_product_nature;
DROP VIEW IF EXISTS v_breadmaking_product_nature;
DROP VIEW IF EXISTS v_sale_product_nature;

-- Drop all functions
DROP FUNCTION IF EXISTS update_ingredient_stock_on_movement();
DROP FUNCTION IF EXISTS calculate_ingredient_cost(INT, INT);
DROP FUNCTION IF EXISTS calculate_profit_by_period(DATE, DATE);
DROP FUNCTION IF EXISTS calculate_average_daily_usage(DATE, DATE);
DROP FUNCTION IF EXISTS generate_ingredient_forecast(INTEGER, INTEGER);
DROP FUNCTION IF EXISTS save_ingredient_forecast(INTEGER, INTEGER);
DROP FUNCTION IF EXISTS check_ingredient_stock_for_production(INTEGER, INTEGER);
DROP FUNCTION IF EXISTS validate_production_stock();
DROP FUNCTION IF EXISTS handle_ingredient_movements_on_production();
DROP FUNCTION IF EXISTS update_product_stock_on_movement();
DROP FUNCTION IF EXISTS handle_product_movements_on_production();
DROP FUNCTION IF EXISTS handle_sale_details_changes();
DROP FUNCTION IF EXISTS create_turnover_on_product_movement();
DROP FUNCTION IF EXISTS create_turnover_on_ingredient_supply();

-- Drop all tables (in correct order due to foreign key constraints)
DROP TABLE IF EXISTS sale_details;
DROP TABLE IF EXISTS sale;
DROP TABLE IF EXISTS product_movement;
DROP TABLE IF EXISTS ingredient_movement;
DROP TABLE IF EXISTS ingredient_forecast;
DROP TABLE IF EXISTS breadmaking;
DROP TABLE IF EXISTS recipe_ingredient;
DROP TABLE IF EXISTS product_recommendations;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS ingredient;
DROP TABLE IF EXISTS turnover;
DROP TABLE IF EXISTS bm_user;
DROP TABLE IF EXISTS gender;

-- Drop all custom ENUMs
DROP TYPE IF EXISTS role;
DROP TYPE IF EXISTS unit;
DROP TYPE IF EXISTS turnover_type;
DROP TYPE IF EXISTS loss_type;
DROP TYPE IF EXISTS product_type;
DROP TYPE IF EXISTS ingredient_type;
DROP TYPE IF EXISTS movement_type;