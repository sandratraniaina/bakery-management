SET
    check_function_bodies = false;

CREATE TYPE role AS ENUM ('Admin', 'Manager', 'Staff');

CREATE TYPE unit AS ENUM ('kg', 'g', 'l', 'ml');

CREATE TYPE turnover_type AS ENUM ('Income', 'Expense');

CREATE TYPE loss_type AS ENUM ('Product', 'Ingredient');

CREATE TABLE bm_user(
    id serial NOT NULL,
    username varchar(50) NOT NULL UNIQUE,
    password_hash text NOT NULL,
    "role" "role" NOT NULL,
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
    cost_per_unit numeric(10, 2) NOT NULL GENERATED ALWAYS AS (ingredient_cost + other_cost) STORED,
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
    id serial NOT NULL,
    recipe_id integer NOT NULL,
    ingredient_id integer NOT NULL,
    quantity numeric(10, 2) NOT NULL,
    CONSTRAINT recipe_ingredient_pkey PRIMARY KEY(recipe_id, ingredient_id)
);

CREATE TABLE sale(
    id serial NOT NULL,
    created_by integer,
    client_name varchar(100) NOT NULL,
    total_price numeric(10, 2) NOT NULL,
    sale_date date NOT NULL,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT sale_pkey1 PRIMARY KEY(id)
);

CREATE TABLE sale_product(
    id integer NOT NULL,
    product_id integer NOT NULL,
    sale_id integer NOT NULL,
    quantity integer NOT NULL,
    unit_price numeric(10, 2) NOT NULL,
    CONSTRAINT sale_product_pkey PRIMARY KEY(id)
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

ALTER TABLE
    recipe_ingredient
ADD
    CONSTRAINT recipe_ingredient_recipe_id_fkey FOREIGN KEY (recipe_id) REFERENCES recipe (id) ON DELETE Cascade;

ALTER TABLE
    recipe_ingredient
ADD
    CONSTRAINT recipe_ingredient_ingredient_id_fkey FOREIGN KEY (ingredient_id) REFERENCES ingredient (id) ON DELETE Cascade;

ALTER TABLE
    product
ADD
    CONSTRAINT product_recipe_id_fkey FOREIGN KEY (recipe_id) REFERENCES recipe (id) ON DELETE
Set
    null;

ALTER TABLE
    breadmaking
ADD
    CONSTRAINT breadmaking_product_id_fkey FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE Cascade;

ALTER TABLE
    breadmaking
ADD
    CONSTRAINT breadmaking_created_by_fkey FOREIGN KEY (created_by) REFERENCES bm_user (id) ON DELETE
Set
    null;

ALTER TABLE
    sale
ADD
    CONSTRAINT sale_created_by_fkey FOREIGN KEY (created_by) REFERENCES bm_user (id);

ALTER TABLE
    ingredient_forecast
ADD
    CONSTRAINT ingredient_forecast_ingredient_id_fkey FOREIGN KEY (ingredient_id) REFERENCES ingredient (id) ON DELETE Cascade;

ALTER TABLE
    loss
ADD
    CONSTRAINT loss_reported_by_fkey FOREIGN KEY (reported_by) REFERENCES bm_user (id) ON DELETE
Set
    null;

ALTER TABLE
    ingredient_supply
ADD
    CONSTRAINT ingredient_supply_ingredient_id_fkey1 FOREIGN KEY (ingredient_id) REFERENCES ingredient (id) ON DELETE
Set
    null;

ALTER TABLE
    sale_product
ADD
    CONSTRAINT sale_product_product_id_fkey FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE
    sale_product
ADD
    CONSTRAINT sale_product_sale_id_fkey FOREIGN KEY (sale_id) REFERENCES sale (id);

