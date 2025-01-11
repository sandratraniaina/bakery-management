package mg.sandratra.bakery.repository.ingredient;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import mg.sandratra.bakery.enums.IngredientType;
import mg.sandratra.bakery.enums.Unit;
import mg.sandratra.bakery.models.ingredient.Ingredient;
import mg.sandratra.bakery.repository.BaseRepository;

@Repository
public class IngredientRepository extends BaseRepository<Ingredient> {

    public IngredientRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<Ingredient> getRowMapper() {
        return new RowMapper<Ingredient>() {
            @Override
            public Ingredient mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(rs.getLong("id"));
                ingredient.setName(rs.getString("name"));
                ingredient.setIngredientType(IngredientType.valueOf(rs.getString("ingredient_type")));
                ingredient.setUnit(Unit.valueOf(rs.getString("unit")));
                ingredient.setCostPerUnit(rs.getBigDecimal("cost_per_unit"));
                ingredient.setStockQuantity(rs.getBigDecimal("stock_quantity"));
                ingredient.setMinimumStock(rs.getBigDecimal("minimum_stock"));
                ingredient.setLastUpdated(rs.getTimestamp("last_updated"));
                return ingredient;
            }
        };
    }

    public List<Ingredient> findAll() {
        String sql = "SELECT * FROM ingredient";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    public Ingredient findById(Long id) {
        String sql = "SELECT * FROM ingredient WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, getRowMapper(), id);
    }

    // Save method: Inserts a new ingredient record
    public int save(Ingredient ingredient) {
        String sql = "INSERT INTO ingredient (name, unit, cost_per_unit, stock_quantity, minimum_stock, ingredient_type) "
                +
                "VALUES (:name, CAST(:unit AS unit), :cost_per_unit, :stock_quantity, :minimum_stock, CAST(:ingredient_type AS ingredient_type))";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", ingredient.getId()) // Assuming `id` is provided for updates
                .addValue("name", ingredient.getName())
                .addValue("unit", ingredient.getUnit().toString())
                .addValue("cost_per_unit", ingredient.getCostPerUnit())
                .addValue("stock_quantity", ingredient.getStockQuantity())
                .addValue("minimum_stock", ingredient.getMinimumStock())
                .addValue("ingredient_type", ingredient.getIngredientType().toString());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    // Update method: Updates an existing ingredient record
    public int update(Ingredient ingredient) {
        String sql = "UPDATE ingredient SET " +
                "name = :name, unit = CAST(:unit AS unit), cost_per_unit = :cost_per_unit, " +
                "stock_quantity = :stock_quantity, minimum_stock = :minimum_stock, " +
                "last_updated = :last_updated, ingredient_type = CAST(:ingredient_type AS ingredient_type) " +
                "WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", ingredient.getId()) // Assuming `id` is provided for updates
                .addValue("name", ingredient.getName())
                .addValue("unit", ingredient.getUnit().toString())
                .addValue("cost_per_unit", ingredient.getCostPerUnit())
                .addValue("stock_quantity", ingredient.getStockQuantity())
                .addValue("minimum_stock", ingredient.getMinimumStock())
                .addValue("last_updated", Timestamp.valueOf(LocalDateTime.now()))
                .addValue("ingredient_type", ingredient.getIngredientType().toString());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM ingredient WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
