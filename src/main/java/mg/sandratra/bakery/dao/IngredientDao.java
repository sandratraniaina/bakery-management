package mg.sandratra.bakery.dao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import mg.sandratra.bakery.enums.Unit;
import mg.sandratra.bakery.models.Ingredient;

@Repository
public class IngredientDao extends BaseDao<Ingredient> {

    public IngredientDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<Ingredient> getRowMapper() {
        return new RowMapper<Ingredient>() {
            @Override
            public Ingredient mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(rs.getInt("id"));
                ingredient.setName(rs.getString("name"));
                ingredient.setUnit(Unit.valueOf(rs.getString("unit").toUpperCase()));
                ingredient.setCostPerUnit(rs.getDouble("cost_per_unit"));
                ingredient.setStockQuantity(rs.getDouble("stock_quantity"));
                ingredient.setMinimumStock(rs.getDouble("minimum_stock"));
                ingredient.setLastUpdated(rs.getTimestamp("last_updated"));
                return ingredient;
            }
        };
    }

    public List<Ingredient> findAll() {
        String sql = "SELECT * FROM ingredient";
        return findAll(sql);
    }

    public Ingredient findById(Long id) {
        String sql = "SELECT * FROM ingredient WHERE id = ?";
        return findById(sql, id);
    }

    // Save method: Inserts a new ingredient record
    public int save(Ingredient ingredient) {
        String sql = "INSERT INTO ingredient (name, unit, cost_per_unit, stock_quantity, minimum_stock) "
                +
                "VALUES (:name, CAST(:unit AS unit), :cost_per_unit, :stock_quantity, :minimum_stock)";

        return saveOrUpdate(sql, new BeanPropertySqlParameterSource(ingredient));
    }

    // Update method: Updates an existing ingredient record
    public int update(Ingredient ingredient) {
        String sql = "UPDATE ingredient SET " +
                "name = :name, unit = CAST(:unit AS unit), cost_per_unit = :cost_per_unit, " +
                "stock_quantity = :stock_quantity, minimum_stock = :minimum_stock, " +
                "last_updated = :last_updated " +
                "WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", ingredient.getId()) // Assuming `id` is provided for updates
                .addValue("name", ingredient.getName())
                .addValue("unit", ingredient.getUnit().toString())
                .addValue("cost_per_unit", ingredient.getCostPerUnit())
                .addValue("stock_quantity", ingredient.getStockQuantity())
                .addValue("minimum_stock", ingredient.getMinimumStock())
                .addValue("last_updated", Timestamp.valueOf(LocalDateTime.now()));

        return saveOrUpdate(sql, params);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM ingredient WHERE id = ?";
        return deleteById(sql, id);
    }
}
