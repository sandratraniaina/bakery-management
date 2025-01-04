CREATE OR REPLACE FUNCTION update_stock_on_loss() 
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.loss_type = 'Ingredient' THEN
        UPDATE ingredient
        SET stock_quantity = stock_quantity - NEW.quantity
        WHERE id = NEW.reference_id; -- Use `id` instead of `reference_id`

    ELSIF NEW.loss_type = 'Product' THEN
        UPDATE product
        SET stock_quantity = stock_quantity - NEW.quantity
        WHERE id = NEW.reference_id; -- Use `id` instead of `reference_id`

    END IF;

    RETURN NEW;

END;
$$ LANGUAGE plpgsql;


CREATE
OR REPLACE FUNCTION update_product_stock_on_production() RETURNS TRIGGER AS $$ BEGIN
UPDATE
    product
SET
    stock_quantity = stock_quantity + NEW.quantity
WHERE
    id = NEW.product_id;

RETURN NEW;

END;

$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_ingredients_stock_on_production() 
RETURNS TRIGGER AS $$
DECLARE
    ingredient_record RECORD; -- Declare the variable as a record type
BEGIN
    -- Loop through each ingredient in the recipe of the produced product
    FOR ingredient_record IN (
        SELECT
            ri.ingredient_id,
            ri.quantity
        FROM
            recipe_ingredient ri
        JOIN product p ON p.recipe_id = ri.recipe_id
        WHERE
            p.id = NEW.product_id
    ) LOOP
        -- Update the stock quantity of the ingredient
        UPDATE ingredient
        SET stock_quantity = stock_quantity - (ingredient_record.quantity * NEW.quantity)
        WHERE id = ingredient_record.ingredient_id;
    END LOOP;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE
OR REPLACE FUNCTION update_ingredient_stock_on_supply() RETURNS TRIGGER AS $$ BEGIN -- Update the stock quantity of the ingredient
UPDATE
    ingredient
SET
    stock_quantity = stock_quantity + NEW.quantity,
    cost_per_unit = CASE
        WHEN NEW.cost_per_unit > cost_per_unit THEN NEW.cost_per_unit
        ELSE cost_per_unit
    END,
    last_updated = CURRENT_TIMESTAMP
WHERE
    id = NEW.ingredient_id;

RETURN NEW;

END;

$$ LANGUAGE plpgsql;

CREATE
OR REPLACE FUNCTION calculate_ingredient_cost(product_id INT, quantity INT) RETURNS NUMERIC AS $$ DECLARE total_cost NUMERIC := 0;

BEGIN -- Calculate the total ingredient cost for the given product and quantity
SELECT
    SUM(ri.quantity * i.cost_per_unit * quantity) INTO total_cost
FROM
    recipe_ingredient ri
    JOIN product p ON ri.recipe_id = p.recipe_id
    JOIN ingredient i ON ri.ingredient_id = i.id
WHERE
    p.id = product_id;

-- Error handling: If total_cost is NULL, it means no ingredients were found
IF total_cost IS NULL THEN RAISE NOTICE 'No ingredients found for product ID %',
product_id;

RETURN 0;

-- Return 0 as the cost in case of no ingredients
END IF;

RETURN total_cost;

END;

$$ LANGUAGE plpgsql;

CREATE
OR REPLACE FUNCTION calculate_profit_by_period(start_date DATE, end_date DATE) RETURNS TABLE (
    product_id INTEGER,
    product_name VARCHAR,
    total_income NUMERIC,
    total_cost_of_production NUMERIC,
    profit NUMERIC
) AS $$ BEGIN RETURN QUERY
SELECT
    p.id AS product_id,
    p.name AS product_name,
    COALESCE(SUM(s.quantity * p.price), 0) AS total_income,
    COALESCE(SUM(b.quantity * b.cost_per_unit), 0) AS total_cost_of_production,
    COALESCE(total_income, 0) - COALESCE(total_cost_of_production, 0) AS profit
FROM
    product p
    LEFT JOIN sale s ON p.id = s.product_id
    AND s.sale_date BETWEEN '2024-01-01'
    AND '2024-12-31'
    LEFT JOIN breadmaking b ON p.id = b.product_id
    AND b.production_date BETWEEN '2024-01-01'
    AND '2024-12-31'
GROUP BY
    p.id,
    p.name
ORDER BY
    profit DESC;

END;

$$ LANGUAGE plpgsql;

CREATE
OR REPLACE FUNCTION insert_turnover_on_sale() RETURNS TRIGGER AS $$ BEGIN
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
        NEW.total_price,
        'Client: ' || NEW.client_name,
        NEW.sale_date
    );

RETURN NEW;

END;

$$ LANGUAGE plpgsql;

CREATE
OR REPLACE FUNCTION insert_turnover_on_production() RETURNS TRIGGER AS $$ BEGIN
INSERT INTO
    turnover (
        turnover_type,
        amount,
        description,
        turnover_date
    )
VALUES
    (
        'Expense',
        NEW.quantity * NEW.cost_per_unit,
        'Production cost for ' || NEW.quantity || 'x ' || (
            SELECT
                name
            FROM
                product
            WHERE
                id = NEW.product_id
        ),
        NEW.production_date
    );

RETURN NEW;

END;

$$ LANGUAGE plpgsql;

CREATE
OR REPLACE FUNCTION insert_turnover_on_supply() RETURNS TRIGGER AS $$ BEGIN
INSERT INTO
    turnover (
        turnover_type,
        amount,
        description,
        turnover_date
    )
VALUES
    (
        'Expense',
        NEW.total_cost,
        'Supply of ' || NEW.quantity || ' ' || (
            SELECT
                unit || ' of ' || name
            FROM
                ingredient
            WHERE
                id = NEW.ingredient_id
        ),
        NEW.supply_date
    );

RETURN NEW;

END;

$$ LANGUAGE plpgsql;

CREATE
OR REPLACE FUNCTION insert_turnover_on_loss() RETURNS TRIGGER AS $$ DECLARE loss_amount numeric(10, 2);

item_name text;

BEGIN -- Calculate loss amount and get item name based on loss type
IF NEW.loss_type = 'Product' THEN
SELECT
    price * NEW.quantity,
    name INTO loss_amount,
    item_name
FROM
    product
WHERE
    id = NEW.reference_id;

ELSE -- Ingredient loss
SELECT
    cost_per_unit * NEW.quantity,
    name INTO loss_amount,
    item_name
FROM
    ingredient
WHERE
    id = NEW.reference_id;

END IF;

INSERT INTO
    turnover (
        turnover_type,
        amount,
        description,
        turnover_date
    )
VALUES
    (
        'Expense',
        loss_amount,
        'Loss of ' || NEW.quantity || ' ' || NEW.loss_type || ': ' || item_name || ' - Reason: ' || NEW.reason,
        NEW.loss_date
    );

RETURN NEW;

END;

$$ LANGUAGE plpgsql;

-- Function to calculate average daily ingredient usage for each ingredient
CREATE
OR REPLACE FUNCTION calculate_average_daily_usage(start_date DATE, end_date DATE) RETURNS TABLE (
    ingredient_id INTEGER,
    ingredient_name VARCHAR,
    avg_daily_usage NUMERIC,
    current_stock NUMERIC,
    minimum_stock NUMERIC
) AS $$ BEGIN RETURN QUERY WITH daily_usage AS (
    SELECT
        v.ingredient_id,
        v.ingredient_name,
        v.production_date,
        v.total_quantity_used
    FROM
        v_daily_ingredient_usage v
    WHERE
        v.production_date BETWEEN start_date
        AND end_date
)
SELECT
    d.ingredient_id,
    d.ingredient_name,
    ROUND(AVG(d.total_quantity_used), 2) as avg_daily_usage,
    i.stock_quantity as current_stock,
    i.minimum_stock
FROM
    daily_usage d
    JOIN ingredient i ON i.id = d.ingredient_id
GROUP BY
    d.ingredient_id,
    d.ingredient_name,
    i.stock_quantity,
    i.minimum_stock;

END;

$$ LANGUAGE plpgsql;

-- Function to generate ingredient forecasts for the next N days
CREATE
OR REPLACE FUNCTION generate_ingredient_forecast(
    days_to_forecast INTEGER,
    lookback_days INTEGER DEFAULT 30
) RETURNS TABLE (
    ingredient_id INTEGER,
    ingredient_name VARCHAR,
    forecast_date DATE,
    forecast_quantity NUMERIC,
    current_stock NUMERIC,
    minimum_stock NUMERIC,
    needs_reorder BOOLEAN
) AS $$ DECLARE start_date DATE;

end_date DATE;

BEGIN -- Calculate date range for historical analysis
start_date := CURRENT_DATE - lookback_days;

end_date := CURRENT_DATE;

RETURN QUERY WITH forecast_base AS (
    -- Get average daily usage
    SELECT
        *
    FROM
        calculate_average_daily_usage(start_date, end_date)
),
forecast_dates AS (
    -- Generate series of dates to forecast
    SELECT
        generate_series(
            CURRENT_DATE + 1,
            CURRENT_DATE + days_to_forecast,
            '1 day' :: interval
        ) :: date AS forecast_date
) -- Generate forecast for each ingredient and date
SELECT
    f.ingredient_id,
    f.ingredient_name,
    d.forecast_date,
    ROUND(f.avg_daily_usage * 1.1, 2) AS forecast_quantity,
    -- Add 10% buffer
    f.current_stock,
    f.minimum_stock,
    (
        f.current_stock - (
            f.avg_daily_usage * (d.forecast_date - CURRENT_DATE)
        )
    ) < f.minimum_stock AS needs_reorder
FROM
    forecast_base f
    CROSS JOIN forecast_dates d
ORDER BY
    d.forecast_date,
    f.ingredient_name;

END;

$$ LANGUAGE plpgsql;

-- Function to save forecasts to ingredient_forecast table
CREATE
OR REPLACE FUNCTION save_ingredient_forecast(
    days_to_forecast INTEGER,
    lookback_days INTEGER DEFAULT 30
) RETURNS void AS $$ BEGIN -- Clear existing forecasts for the forecast period
DELETE FROM
    ingredient_forecast
WHERE
    forecast_date > CURRENT_DATE
    AND forecast_date <= CURRENT_DATE + days_to_forecast;

-- Insert new forecasts
INSERT INTO
    ingredient_forecast (
        ingredient_id,
        forecast_date,
        forecast_quantity
    )
SELECT
    ingredient_id,
    forecast_date,
    forecast_quantity
FROM
    generate_ingredient_forecast(days_to_forecast, lookback_days);

END;

$$ LANGUAGE plpgsql;

CREATE
OR REPLACE VIEW v_daily_ingredient_usage AS
SELECT
    ri.ingredient_id,
    i.name AS ingredient_name,
    SUM(ri.quantity * b.quantity) AS total_quantity_used,
    b.production_date
FROM
    breadmaking b
    JOIN product p ON b.product_id = p.id
    JOIN recipe r ON p.recipe_id = r.id
    JOIN recipe_ingredient ri ON r.id = ri.recipe_id
    JOIN ingredient i ON ri.ingredient_id = i.id
GROUP BY
    ri.ingredient_id,
    i.name,
    b.production_date
ORDER BY
    b.production_date,
    i.name;

CREATE
OR REPLACE VIEW v_weekly_ingredient_usage AS
SELECT
    ri.ingredient_id,
    i.name AS ingredient_name,
    SUM(ri.quantity * b.quantity) AS total_quantity_used,
    DATE_TRUNC('week', b.production_date) AS week_start_date
FROM
    breadmaking b
    JOIN product p ON b.product_id = p.id
    JOIN recipe r ON p.recipe_id = r.id
    JOIN recipe_ingredient ri ON r.id = ri.recipe_id
    JOIN ingredient i ON ri.ingredient_id = i.id
GROUP BY
    ri.ingredient_id,
    i.name,
    week_start_date
ORDER BY
    week_start_date,
    i.name;

CREATE
OR REPLACE VIEW v_yearly_ingredient_usage AS
SELECT
    ri.ingredient_id,
    i.name AS ingredient_name,
    SUM(ri.quantity * b.quantity) AS total_quantity_used,
    DATE_TRUNC('year', b.production_date) AS year_start_date
FROM
    breadmaking b
    JOIN product p ON b.product_id = p.id
    JOIN recipe r ON p.recipe_id = r.id
    JOIN recipe_ingredient ri ON r.id = ri.recipe_id
    JOIN ingredient i ON ri.ingredient_id = i.id
GROUP BY
    ri.ingredient_id,
    i.name,
    year_start_date
ORDER BY
    year_start_date,
    i.name;

-- View to show ingredients that need reordering
CREATE
OR REPLACE VIEW v_ingredients_to_reorder AS WITH forecast AS (
    SELECT
        i.id AS ingredient_id,
        i.name AS ingredient_name,
        i.stock_quantity AS current_stock,
        i.minimum_stock,
        i.unit,
        COALESCE(
            (
                SELECT
                    SUM(forecast_quantity)
                FROM
                    ingredient_forecast f
                WHERE
                    f.ingredient_id = i.id
                    AND f.forecast_date <= CURRENT_DATE + 7
            ),
            0
        ) AS forecast_usage_7_days
    FROM
        ingredient i
)
SELECT
    ingredient_id,
    ingredient_name,
    current_stock,
    minimum_stock,
    unit,
    forecast_usage_7_days,
    current_stock - forecast_usage_7_days AS projected_stock,
    CASE
        WHEN current_stock - forecast_usage_7_days < minimum_stock THEN TRUE
        ELSE FALSE
    END AS needs_reorder
FROM
    forecast
WHERE
    current_stock - forecast_usage_7_days < minimum_stock
ORDER BY
    ingredient_name;

CREATE OR REPLACE TRIGGER trg_update_stock_on_loss
AFTER
INSERT
    ON loss FOR EACH ROW EXECUTE FUNCTION update_stock_on_loss();

CREATE OR REPLACE TRIGGER trg_update_product_stock_on_production
AFTER
INSERT
    ON breadmaking FOR EACH ROW EXECUTE FUNCTION update_product_stock_on_production();

CREATE OR REPLACE TRIGGER trg_update_ingredients_stock_on_production
AFTER
INSERT
    ON breadmaking FOR EACH ROW EXECUTE FUNCTION update_ingredients_stock_on_production();

CREATE OR REPLACE TRIGGER trg_update_ingredient_stock_on_supply
AFTER
INSERT
    ON ingredient_supply FOR EACH ROW EXECUTE FUNCTION update_ingredient_stock_on_supply();

CREATE OR REPLACE TRIGGER trg_insert_turnover_on_sale
AFTER
INSERT
    ON sale FOR EACH ROW EXECUTE FUNCTION insert_turnover_on_sale();

CREATE OR REPLACE TRIGGER trg_insert_turnover_on_production
AFTER
INSERT
    ON breadmaking FOR EACH ROW EXECUTE FUNCTION insert_turnover_on_production();

CREATE OR REPLACE TRIGGER trg_insert_turnover_on_supply
AFTER
INSERT
    ON ingredient_supply FOR EACH ROW EXECUTE FUNCTION insert_turnover_on_supply();

CREATE OR REPLACE TRIGGER trg_insert_turnover_on_loss
AFTER
INSERT
    ON loss FOR EACH ROW EXECUTE FUNCTION insert_turnover_on_loss();