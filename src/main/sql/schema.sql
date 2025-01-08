SET check_function_bodies = false
;

CREATE TYPE role AS ENUM ('ADMIN', 'MANAGER', 'STAFF');

CREATE TYPE unit AS ENUM ('KG', 'G', 'L', 'ML');

CREATE TYPE turnover_type AS ENUM ('Income', 'Expense');

CREATE TYPE loss_type AS ENUM ('Product', 'Ingredient');

CREATE TABLE bm_user(
  id serial NOT NULL,
  username varchar(50) NOT NULL UNIQUE,
  password_hash text NOT NULL,
  "role" "role" NOT NULL,
  enabled boolean NOT NULL DEFAULT true,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT bm_user_pkey PRIMARY KEY(id)
);

CREATE TABLE breadmaking(
  id serial NOT NULL,
  product_id integer NOT NULL,
  created_by integer,
  quantity integer NOT NULL,
  production_date date NOT NULL,
  ingredient_cost numeric(10, 2) NOT NULL,
  other_cost numeric(10, 2) NOT NULL DEFAULT 0,
  cost_per_unit numeric(10, 2) NOT NULL
  GENERATED ALWAYS AS (ingredient_cost + other_cost) STORED,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT breadmaking_pkey PRIMARY KEY(id)
);

CREATE TABLE ingredient(
  id serial NOT NULL,
  "name" varchar(100) NOT NULL,
  unit unit NOT NULL,
  cost_per_unit numeric(10, 2) NOT NULL,
  stock_quantity numeric(10, 2) NOT NULL,
  minimum_stock numeric(10, 2) NOT NULL,
  last_updated timestamp DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT ingredient_pkey1 PRIMARY KEY(id)
);

CREATE TABLE ingredient_forecast(
  id serial NOT NULL,
  ingredient_id integer NOT NULL,
  forecast_date date NOT NULL,
  forecast_quantity numeric(10, 2) NOT NULL,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT ingredient_forecast_pkey PRIMARY KEY(id)
);

CREATE TABLE ingredient_supply(
  id serial NOT NULL,
  ingredient_id integer,
  quantity numeric(10, 2) NOT NULL,
  supply_date date NOT NULL,
  cost_per_unit numeric(10, 2) NOT NULL,
  total_cost numeric(10, 2) GENERATED ALWAYS AS (quantity * cost_per_unit) STORED,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT ingredient_supply_pkey1 PRIMARY KEY(id)
);

CREATE TABLE loss(
  id serial NOT NULL,
  reported_by integer NOT NULL,
  loss_type loss_type NOT NULL,
  reference_id integer,
  reference_name text,
  quantity numeric(10, 2) NOT NULL,
  reason text NOT NULL,
  loss_date date NOT NULL,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT loss_pkey PRIMARY KEY(id)
);

CREATE TABLE product(
  id serial NOT NULL,
  recipe_id integer,
  "name" varchar(100) NOT NULL,
  price numeric(10, 2) NOT NULL,
  stock_quantity integer NOT NULL,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT product_pkey1 PRIMARY KEY(id)
);

CREATE TABLE recipe(
  id serial NOT NULL,
  "name" varchar(100) NOT NULL,
  description text,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT recipe_pkey1 PRIMARY KEY(id)
);

CREATE TABLE recipe_ingredient(
  recipe_id integer NOT NULL,
  ingredient_id integer NOT NULL,
  quantity numeric(10, 2) NOT NULL,
  CONSTRAINT recipe_ingredient_pkey PRIMARY KEY(recipe_id, ingredient_id)
);

CREATE TABLE sale(
  id serial NOT NULL,
  created_by integer,
  total_amount numeric(10, 2) NOT NULL,
  sale_date date NOT NULL,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT sale_pkey1 PRIMARY KEY(id)
);

CREATE TABLE sale_details(
  id serial NOT NULL,
  sale_id integer NOT NULL,
  product_id integer NOT NULL,
  quantity integer NOT NULL,
  unit_price numeric(10, 2) NOT NULL,
  subtotal numeric(10, 2) NOT NULL
  GENERATED ALWAYS AS (quantity * unit_price) STORED,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT sale_details_pkey PRIMARY KEY(id)
);

CREATE TABLE turnover(
  id serial NOT NULL,
  turnover_type turnover_type NOT NULL,
  amount numeric(10, 2) NOT NULL,
  description text,
  turnover_date date NOT NULL,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT turnover_pkey PRIMARY KEY(id)
);

ALTER TABLE recipe_ingredient
  ADD CONSTRAINT recipe_ingredient_recipe_id_fkey
    FOREIGN KEY (recipe_id) REFERENCES recipe (id) ON DELETE Cascade;

ALTER TABLE recipe_ingredient
  ADD CONSTRAINT recipe_ingredient_ingredient_id_fkey
    FOREIGN KEY (ingredient_id) REFERENCES ingredient (id) ON DELETE Cascade;

ALTER TABLE product
  ADD CONSTRAINT product_recipe_id_fkey
    FOREIGN KEY (recipe_id) REFERENCES recipe (id) ON DELETE Set null;

ALTER TABLE breadmaking
  ADD CONSTRAINT breadmaking_product_id_fkey
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE Cascade;

ALTER TABLE breadmaking
  ADD CONSTRAINT breadmaking_created_by_fkey
    FOREIGN KEY (created_by) REFERENCES bm_user (id) ON DELETE Set null;

ALTER TABLE sale
  ADD CONSTRAINT sale_created_by_fkey
    FOREIGN KEY (created_by) REFERENCES bm_user (id);

ALTER TABLE ingredient_forecast
  ADD CONSTRAINT ingredient_forecast_ingredient_id_fkey
    FOREIGN KEY (ingredient_id) REFERENCES ingredient (id) ON DELETE Cascade;

ALTER TABLE loss
  ADD CONSTRAINT loss_reported_by_fkey
    FOREIGN KEY (reported_by) REFERENCES bm_user (id) ON DELETE Set null;

ALTER TABLE ingredient_supply
  ADD CONSTRAINT ingredient_supply_ingredient_id_fkey1
    FOREIGN KEY (ingredient_id) REFERENCES ingredient (id) ON DELETE Set null;

ALTER TABLE sale_details
  ADD CONSTRAINT sale_details_sale_id_fkey
    FOREIGN KEY (sale_id) REFERENCES sale (id);

ALTER TABLE sale_details
  ADD CONSTRAINT sale_details_product_id_fkey
    FOREIGN KEY (product_id) REFERENCES product (id);

CREATE OR REPLACE FUNCTION update_stock_on_loss()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.loss_type = 'Ingredient' THEN
        UPDATE ingredient
        SET stock_quantity = stock_quantity - NEW.quantity
        WHERE id = NEW.reference_id;
    ELSIF NEW.loss_type = 'Product' THEN
        UPDATE product
        SET stock_quantity = stock_quantity - NEW.quantity
        WHERE id = NEW.reference_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION update_product_stock_on_production()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE product
    SET stock_quantity = stock_quantity + NEW.quantity
    WHERE id = NEW.product_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_ingredients_stock_on_production()
RETURNS TRIGGER AS $$
DECLARE
    ingredient_id INT;
    ingredient_quantity NUMERIC;
BEGIN
    -- Loop through each ingredient in the recipe of the produced product
    FOR ingredient_id, ingredient_quantity IN (
        SELECT ri.ingredient_id, ri.quantity
        FROM recipe_ingredient ri
        JOIN product p ON p.recipe_id = ri.recipe_id
        WHERE p.id = NEW.product_id
    )
    LOOP
        -- Update the stock quantity of the ingredient
        UPDATE ingredient
        SET stock_quantity = stock_quantity - (ingredient_quantity * NEW.quantity)
        WHERE id = ingredient_id;
    END LOOP;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION update_ingredient_stock_on_supply()
RETURNS TRIGGER AS $$
BEGIN
    -- Update the stock quantity of the ingredient
    UPDATE ingredient
    SET 
        stock_quantity = stock_quantity + NEW.quantity,
        cost_per_unit = CASE
            WHEN NEW.cost_per_unit > cost_per_unit THEN NEW.cost_per_unit
            ELSE cost_per_unit
        END,
        last_updated = CURRENT_TIMESTAMP
    WHERE id = NEW.ingredient_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION calculate_ingredient_cost(product_id INT, batch_size INT)
RETURNS NUMERIC AS $$
DECLARE
    total_cost NUMERIC := 0;
BEGIN
    -- Calculate the total ingredient cost for the given product and quantity
    SELECT SUM(ri.quantity * i.cost_per_unit * $2)
    INTO total_cost
    FROM recipe_ingredient ri
    JOIN product p ON ri.recipe_id = p.recipe_id
    JOIN ingredient i ON ri.ingredient_id = i.id
    WHERE p.id = $1;

    -- Error handling: If total_cost is NULL, it means no ingredients were found
    IF total_cost IS NULL THEN
        RAISE NOTICE 'No ingredients found for product ID %', product_id;
        RETURN 0; -- Return 0 as the cost in case of no ingredients
    END IF;

    RETURN total_cost;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION calculate_profit_by_period(start_date DATE, end_date DATE)
RETURNS TABLE (
    product_id INTEGER,
    product_name VARCHAR,
    total_income NUMERIC,
    total_cost_of_production NUMERIC,
    profit NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    WITH production_costs AS (
        SELECT
            p.id AS prod_id,
            p.name AS prod_name,
            COALESCE(SUM(b.quantity * b.cost_per_unit), 0) AS total_cost
        FROM product p
        LEFT JOIN breadmaking b ON p.id = b.product_id 
            AND b.production_date BETWEEN start_date AND end_date
        GROUP BY p.id, p.name
    ),
    sales_income AS (
        SELECT
            p.id AS prod_id,
            p.name AS prod_name,
            COALESCE(SUM(sd.quantity * sd.unit_price), 0) AS total_income
        FROM product p
        LEFT JOIN sale_details sd ON p.id = sd.product_id 
        LEFT JOIN sale s ON sd.sale_id = s.id
            AND s.sale_date BETWEEN start_date AND end_date
        GROUP BY p.id, p.name
    )
    SELECT
        p.id AS product_id,
        p.name AS product_name,
        si.total_income,
        pc.total_cost AS total_cost_of_production,
        si.total_income - pc.total_cost AS profit
    FROM product p
    LEFT JOIN production_costs pc ON p.id = pc.prod_id
    LEFT JOIN sales_income si ON p.id = si.prod_id
    ORDER BY (si.total_income - pc.total_cost) DESC;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION insert_turnover_on_sale()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO turnover (
        turnover_type,
        amount,
        description,
        turnover_date
    ) VALUES (
        'Income',
        NEW.total_amount,
        'Sale invoice #' || NEW.id || ' for ' || COALESCE(NEW.customer_name, 'Walk-in Customer'),
        NEW.sale_date
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION insert_turnover_on_production()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO turnover (
        turnover_type,
        amount,
        description,
        turnover_date
    ) VALUES (
        'Expense',
        NEW.quantity * NEW.cost_per_unit,
        'Production cost for ' || NEW.quantity || 'x ' || (SELECT name FROM product WHERE id = NEW.product_id),
        NEW.production_date
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION insert_turnover_on_supply()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO turnover (
        turnover_type,
        amount,
        description,
        turnover_date
    ) VALUES (
        'Expense',
        NEW.total_cost,
        'Supply of ' || NEW.quantity || ' ' || 
        (SELECT unit || ' of ' || name FROM ingredient WHERE id = NEW.ingredient_id),
        NEW.supply_date
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION insert_turnover_on_loss()
RETURNS TRIGGER AS $$
DECLARE
    loss_amount numeric(10,2);
    item_name text;
BEGIN
    -- Calculate loss amount and get item name based on loss type
    IF NEW.loss_type = 'Product' THEN
        SELECT price * NEW.quantity, name INTO loss_amount, item_name
        FROM product
        WHERE id = NEW.reference_id;
    ELSE -- Ingredient loss
        SELECT cost_per_unit * NEW.quantity, name INTO loss_amount, item_name
        FROM ingredient
        WHERE id = NEW.reference_id;
    END IF;

    INSERT INTO turnover (
        turnover_type,
        amount,
        description,
        turnover_date
    ) VALUES (
        'Expense',
        loss_amount,
        'Loss of ' || NEW.quantity || ' ' || NEW.loss_type || ': ' || item_name || 
        ' - Reason: ' || NEW.reason,
        NEW.loss_date
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION calculate_average_daily_usage(
    start_date DATE,
    end_date DATE
)
RETURNS TABLE (
    ingredient_id INTEGER,
    ingredient_name VARCHAR,
    avg_daily_usage NUMERIC,
    current_stock NUMERIC,
    minimum_stock NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    WITH total_usage AS (
        SELECT 
            v.ingredient_id,
            v.ingredient_name,
            SUM(v.total_quantity_used) as total_quantity_used
        FROM v_daily_ingredient_usage v
        WHERE v.production_date BETWEEN start_date + 1 AND end_date
        GROUP BY v.ingredient_id, v.ingredient_name
    )
    SELECT 
        t.ingredient_id,
        t.ingredient_name,
        ROUND(t.total_quantity_used / (end_date - start_date), 2) as avg_daily_usage,
        i.stock_quantity as current_stock,
        i.minimum_stock
    FROM total_usage t
    JOIN ingredient i ON i.id = t.ingredient_id;
END;
$$ LANGUAGE plpgsql;

-- Function to generate ingredient forecasts for the next N days
CREATE OR REPLACE FUNCTION generate_ingredient_forecast(
    days_to_forecast INTEGER,
    lookback_days INTEGER DEFAULT 30
)
RETURNS TABLE (
    ingredient_id INTEGER,
    ingredient_name VARCHAR,
    forecast_date DATE,
    forecast_quantity NUMERIC,
    current_stock NUMERIC,
    minimum_stock NUMERIC,
    needs_reorder BOOLEAN
) AS $$
DECLARE
    start_date DATE;
    end_date DATE;
BEGIN
    -- Calculate date range for historical analysis
    start_date := CURRENT_DATE - lookback_days;
    end_date := CURRENT_DATE;

    RETURN QUERY
    WITH forecast_base AS (
        -- Get average daily usage
        SELECT *
        FROM calculate_average_daily_usage(start_date, end_date)
    ),
    forecast_dates AS (
        -- Generate series of dates to forecast
        SELECT generate_series(
            CURRENT_DATE + 1,
            CURRENT_DATE + days_to_forecast,
            '1 day'::interval
        )::date AS forecast_date
    )
    -- Generate forecast for each ingredient and date
    SELECT
        f.ingredient_id,
        f.ingredient_name,
        d.forecast_date,
        ROUND(f.avg_daily_usage * 1.1, 2) AS forecast_quantity, -- Add 10% buffer
        f.current_stock,
        f.minimum_stock,
        (f.current_stock - (f.avg_daily_usage * 
            (d.forecast_date - CURRENT_DATE))) < f.minimum_stock AS needs_reorder
    FROM forecast_base f
    CROSS JOIN forecast_dates d
    ORDER BY d.forecast_date, f.ingredient_name;
END;
$$ LANGUAGE plpgsql;

-- Function to save forecasts to ingredient_forecast table
CREATE OR REPLACE FUNCTION save_ingredient_forecast(
    days_to_forecast INTEGER,
    lookback_days INTEGER DEFAULT 30
) RETURNS void AS $$
BEGIN
    -- Clear existing forecasts for the forecast period
    DELETE FROM ingredient_forecast
    WHERE forecast_date > CURRENT_DATE
    AND forecast_date <= CURRENT_DATE + days_to_forecast;

    -- Insert new forecasts
    INSERT INTO ingredient_forecast (
        ingredient_id,
        forecast_date,
        forecast_quantity
    )
    SELECT
        ingredient_id,
        forecast_date,
        forecast_quantity
    FROM generate_ingredient_forecast(days_to_forecast, lookback_days);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_product_stock_on_sale()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE product
    SET stock_quantity = stock_quantity - NEW.quantity
    WHERE id = NEW.product_id;
    
    -- Update the total_amount in the sale table
    UPDATE sale
    SET total_amount = (
        SELECT SUM(subtotal)
        FROM sale_details
        WHERE sale_id = NEW.sale_id
    )
    WHERE id = NEW.sale_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Function to check if there's enough ingredient stock for production
CREATE OR REPLACE FUNCTION check_ingredient_stock_for_production(
    product_id_param INTEGER,
    quantity_param INTEGER
) RETURNS TABLE (
    ingredient_id INTEGER,
    ingredient_name VARCHAR,
    required_quantity NUMERIC,
    available_quantity NUMERIC,
    is_sufficient BOOLEAN
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        i.id AS ingredient_id,
        i.name AS ingredient_name,
        (ri.quantity * quantity_param) AS required_quantity,
        i.stock_quantity AS available_quantity,
        i.stock_quantity >= (ri.quantity * quantity_param) AS is_sufficient
    FROM recipe_ingredient ri
    JOIN product p ON p.recipe_id = ri.recipe_id
    JOIN ingredient i ON i.id = ri.ingredient_id
    WHERE p.id = product_id_param;
END;
$$ LANGUAGE plpgsql;

-- Function to validate stock before production
CREATE OR REPLACE FUNCTION validate_production_stock()
RETURNS TRIGGER AS $$
DECLARE
    insufficient_ingredients TEXT;
BEGIN
    -- Build a string of insufficient ingredients
    SELECT string_agg(ingredient_name || ' (Need: ' || required_quantity || ' ' || i.unit || 
                     ', Available: ' || available_quantity || ' ' || i.unit || ')',
                     ', ')
    INTO insufficient_ingredients
    FROM check_ingredient_stock_for_production(NEW.product_id, NEW.quantity) c
    JOIN ingredient i ON i.id = c.ingredient_id
    WHERE NOT c.is_sufficient;

    -- If there are any insufficient ingredients, raise an error
    IF insufficient_ingredients IS NOT NULL THEN
        RAISE EXCEPTION 'Insufficient stock for production: %', insufficient_ingredients;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE VIEW v_daily_ingredient_usage AS
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
    ri.ingredient_id, i.name, b.production_date
ORDER BY
    b.production_date, i.name;


CREATE OR REPLACE VIEW v_weekly_ingredient_usage AS
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
    ri.ingredient_id, i.name, week_start_date
ORDER BY
    week_start_date, i.name;


CREATE OR REPLACE VIEW v_yearly_ingredient_usage AS
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
    ri.ingredient_id, i.name, year_start_date
ORDER BY
    year_start_date, i.name;


-- View to show ingredients that need reordering
CREATE OR REPLACE VIEW v_ingredients_to_reorder AS
WITH forecast AS (
    SELECT
        i.id AS ingredient_id,
        i.name AS ingredient_name,
        i.stock_quantity AS current_stock,
        i.minimum_stock,
        i.unit,
        COALESCE(
            (SELECT SUM(forecast_quantity)
             FROM ingredient_forecast f
             WHERE f.ingredient_id = i.id
             AND f.forecast_date <= CURRENT_DATE + 7), 0
        ) AS forecast_usage_7_days
    FROM ingredient i
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
        WHEN current_stock - forecast_usage_7_days < minimum_stock 
        THEN TRUE 
        ELSE FALSE 
    END AS needs_reorder
FROM forecast
WHERE current_stock - forecast_usage_7_days < minimum_stock
ORDER BY ingredient_name;

CREATE TRIGGER 
trg_update_stock_on_loss
AFTER INSERT ON loss
FOR EACH ROW
EXECUTE FUNCTION 
update_stock_on_loss();

CREATE TRIGGER trg_update_product_stock_on_production
AFTER INSERT ON breadmaking
FOR EACH ROW
EXECUTE FUNCTION update_product_stock_on_production();

CREATE TRIGGER trg_update_ingredients_stock_on_production
AFTER INSERT ON breadmaking
FOR EACH ROW
EXECUTE FUNCTION update_ingredients_stock_on_production();

CREATE TRIGGER trg_update_ingredient_stock_on_supply
AFTER INSERT ON ingredient_supply
FOR EACH ROW
EXECUTE FUNCTION update_ingredient_stock_on_supply();


CREATE TRIGGER trg_insert_turnover_on_sale
AFTER INSERT ON sale
FOR EACH ROW
EXECUTE FUNCTION insert_turnover_on_sale();

CREATE TRIGGER trg_insert_turnover_on_production
AFTER INSERT ON breadmaking
FOR EACH ROW
EXECUTE FUNCTION insert_turnover_on_production();

CREATE TRIGGER trg_insert_turnover_on_supply
AFTER INSERT ON ingredient_supply
FOR EACH ROW
EXECUTE FUNCTION insert_turnover_on_supply();

CREATE TRIGGER trg_insert_turnover_on_loss
AFTER INSERT ON loss
FOR EACH ROW
EXECUTE FUNCTION insert_turnover_on_loss();

CREATE TRIGGER trg_update_product_stock_on_sale_details
AFTER INSERT ON sale_details
FOR EACH ROW
EXECUTE FUNCTION update_product_stock_on_sale();

CREATE TRIGGER trg_validate_production_stock
BEFORE INSERT ON breadmaking
FOR EACH ROW
EXECUTE FUNCTION validate_production_stock();
