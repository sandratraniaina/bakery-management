CREATE OR REPLACE VIEW v_breadmaking_ingredients AS
SELECT 
    bm.id AS id,
    bm.product_id,
    bm.quantity AS quantity,
    bm.production_date,
    bm.created_by,
    bm.ingredient_cost,
    bm.other_cost,
    bm.cost_per_unit,
    i.id AS ingredient_id
FROM 
    breadmaking bm
JOIN product p ON bm.product_id = p.id
JOIN recipe r ON p.recipe_id = r.id
JOIN recipe_ingredient ri ON r.id = ri.recipe_id
JOIN ingredient i ON ri.ingredient_id = i.id
ORDER BY bm.production_date, bm.id, i.name;