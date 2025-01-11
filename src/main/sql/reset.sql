-- Drop triggers
DROP TRIGGER IF EXISTS trigger_create_turnover_on_ingredient_supply ON ingredient_movement;
DROP TRIGGER IF EXISTS trg_create_turnover_on_product_movement ON product_movement;
DROP TRIGGER IF EXISTS tgr_generate_movement_on_sale ON sale_details;
DROP TRIGGER IF EXISTS tgr_generate_movement_on_production ON breadmaking;
DROP TRIGGER IF EXISTS tgr_update_product_stock_on_movement ON product_movement;
DROP TRIGGER IF EXISTS after_breadmaking_insert ON breadmaking;
DROP TRIGGER IF EXISTS trg_validate_production_stock ON breadmaking;
DROP TRIGGER IF EXISTS trg_update_ingredient_stock_on_movement ON ingredient_movement;

-- Drop views
DROP VIEW IF EXISTS v_breadmaking_ingredients;
DROP VIEW IF EXISTS v_ingredients_to_reorder;
DROP VIEW IF EXISTS v_yearly_ingredient_usage;
DROP VIEW IF EXISTS v_weekly_ingredient_usage;
DROP VIEW IF EXISTS v_daily_ingredient_usage;

-- Drop functions
DROP FUNCTION IF EXISTS create_turnover_on_ingredient_supply();
DROP FUNCTION IF EXISTS create_turnover_on_product_movement();
DROP FUNCTION IF EXISTS generate_movement_on_sale();
DROP FUNCTION IF EXISTS generate_movement_on_production();
DROP FUNCTION IF EXISTS update_product_stock_on_movement();
DROP FUNCTION IF EXISTS generate_ingredient_movement_on_production();
DROP FUNCTION IF EXISTS validate_production_stock();
DROP FUNCTION IF EXISTS check_ingredient_stock_for_production(INTEGER, INTEGER);
DROP FUNCTION IF EXISTS save_ingredient_forecast(INTEGER, INTEGER);
DROP FUNCTION IF EXISTS generate_ingredient_forecast(INTEGER, INTEGER);
DROP FUNCTION IF EXISTS calculate_average_daily_usage(DATE, DATE);
DROP FUNCTION IF EXISTS calculate_profit_by_period(DATE, DATE);
DROP FUNCTION IF EXISTS calculate_ingredient_cost(INTEGER, INTEGER);
DROP FUNCTION IF EXISTS update_ingredient_stock_on_movement();

-- Drop tables with CASCADE for foreign key constraints
DROP TABLE IF EXISTS sale_details CASCADE;
DROP TABLE IF EXISTS sale CASCADE;
DROP TABLE IF EXISTS recipe_ingredient CASCADE;
DROP TABLE IF EXISTS recipe CASCADE;
DROP TABLE IF EXISTS product_movement CASCADE;
DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS ingredient_movement CASCADE;
DROP TABLE IF EXISTS ingredient_forecast CASCADE;
DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS breadmaking CASCADE;
DROP TABLE IF EXISTS bm_user CASCADE;
DROP TABLE IF EXISTS turnover CASCADE;

-- Drop custom types/enums
DROP TYPE IF EXISTS movement_type CASCADE;
DROP TYPE IF EXISTS ingredient_type CASCADE;
DROP TYPE IF EXISTS product_type CASCADE;
DROP TYPE IF EXISTS loss_type CASCADE;
DROP TYPE IF EXISTS turnover_type CASCADE;
DROP TYPE IF EXISTS unit CASCADE;
DROP TYPE IF EXISTS role CASCADE;