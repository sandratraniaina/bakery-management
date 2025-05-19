-- Drop all triggers first to avoid dependency issues
DROP TRIGGER IF EXISTS trg_update_ingredient_stock_on_movement ON ingredient_movement CASCADE;
DROP TRIGGER IF EXISTS trg_validate_production_stock ON breadmaking CASCADE;
DROP TRIGGER IF EXISTS trg_handle_ingredient_movements_on_production ON breadmaking CASCADE;
DROP TRIGGER IF EXISTS tgr_update_product_stock_on_movement ON product_movement CASCADE;
DROP TRIGGER IF EXISTS trg_handle_product_movements_on_production ON breadmaking CASCADE;
DROP TRIGGER IF EXISTS trg_handle_sale_details_changes ON sale_details CASCADE;
DROP TRIGGER IF EXISTS trg_create_turnover_on_product_movement ON product_movement CASCADE;
DROP TRIGGER IF EXISTS trigger_create_turnover_on_ingredient_supply ON ingredient_movement CASCADE;
DROP TRIGGER IF EXISTS tgr_add_price_history_on_product_update ON product CASCADE;
DROP TRIGGER IF EXISTS trg_handle_sale_details_price ON product CASCADE;

-- Drop all views
DROP VIEW IF EXISTS v_daily_ingredient_usage CASCADE;
DROP VIEW IF EXISTS v_weekly_ingredient_usage CASCADE;
DROP VIEW IF EXISTS v_yearly_ingredient_usage CASCADE;
DROP VIEW IF EXISTS v_ingredients_to_reorder CASCADE;
DROP VIEW IF EXISTS v_breadmaking_ingredients CASCADE;
DROP VIEW IF EXISTS v_product_nature CASCADE;
DROP VIEW IF EXISTS v_breadmaking_product_nature CASCADE;
DROP VIEW IF EXISTS v_sale_product_nature CASCADE;
DROP VIEW IF EXISTS v_user_gender CASCADE;

-- Drop all functions
DROP FUNCTION IF EXISTS update_ingredient_stock_on_movement() CASCADE;
DROP FUNCTION IF EXISTS calculate_ingredient_cost(INT, INT) CASCADE;
DROP FUNCTION IF EXISTS calculate_profit_by_period(DATE, DATE) CASCADE;
DROP FUNCTION IF EXISTS calculate_average_daily_usage(DATE, DATE) CASCADE;
DROP FUNCTION IF EXISTS generate_ingredient_forecast(INTEGER, INTEGER) CASCADE;
DROP FUNCTION IF EXISTS save_ingredient_forecast(INTEGER, INTEGER) CASCADE;
DROP FUNCTION IF EXISTS check_ingredient_stock_for_production(INTEGER, INTEGER) CASCADE;
DROP FUNCTION IF EXISTS validate_production_stock() CASCADE;
DROP FUNCTION IF EXISTS handle_ingredient_movements_on_production() CASCADE;
DROP FUNCTION IF EXISTS update_product_stock_on_movement() CASCADE;
DROP FUNCTION IF EXISTS handle_product_movements_on_production() CASCADE;
DROP FUNCTION IF EXISTS handle_sale_details_changes() CASCADE;
DROP FUNCTION IF EXISTS create_turnover_on_product_movement() CASCADE;
DROP FUNCTION IF EXISTS create_turnover_on_ingredient_supply() CASCADE;
DROP FUNCTION IF EXISTS add_price_history_on_product_update() CASCADE;
DROP FUNCTION IF EXISTS handle_sale_details_price() CASCADE;
DROP FUNCTION IF EXISTS get_product_price(DATE, INTEGER) CASCADE;

-- Drop all tables (order doesn't matter now due to CASCADE)
DROP TABLE IF EXISTS product_price_history CASCADE;
DROP TABLE IF EXISTS sale_details CASCADE;
DROP TABLE IF EXISTS sale CASCADE;
DROP TABLE IF EXISTS product_movement CASCADE;
DROP TABLE IF EXISTS ingredient_movement CASCADE;
DROP TABLE IF EXISTS ingredient_forecast CASCADE;
DROP TABLE IF EXISTS breadmaking CASCADE;
DROP TABLE IF EXISTS recipe_ingredient CASCADE;
DROP TABLE IF EXISTS product_recommendations CASCADE;
DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS recipe CASCADE;
DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS turnover CASCADE;
DROP TABLE IF EXISTS bm_user CASCADE;
DROP TABLE IF EXISTS gender CASCADE;

-- Drop all custom ENUMs
DROP TYPE IF EXISTS role CASCADE;
DROP TYPE IF EXISTS unit CASCADE;
DROP TYPE IF EXISTS turnover_type CASCADE;
DROP TYPE IF EXISTS loss_type CASCADE;
DROP TYPE IF EXISTS product_type CASCADE;
DROP TYPE IF EXISTS ingredient_type CASCADE;
DROP TYPE IF EXISTS movement_type CASCADE;