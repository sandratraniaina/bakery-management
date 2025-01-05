package mg.sandratra.bakery.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
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

    public int saveOrUpdate(Ingredient ingredient) {
        String sql = "INSERT INTO ingredient (name, unit, cost_per_unit, stock_quantity, minimum_stock, last_updated) " +
                     "VALUES (:name, :unit, :cost_per_unit, :stock_quantity, :minimum_stock, :last_updated) " +
                     "ON CONFLICT(id) DO UPDATE SET " +
                     "name = :name, unit = :unit, cost_per_unit = :cost_per_unit, stock_quantity = :stock_quantity, " +
                     "minimum_stock = :minimum_stock, last_updated = :last_updated";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", ingredient.getName())
                .addValue("unit", ingredient.getUnit().toString())
                .addValue("cost_per_unit", ingredient.getCostPerUnit())
                .addValue("stock_quantity", ingredient.getStockQuantity())
                .addValue("minimum_stock", ingredient.getMinimumStock())
                .addValue("last_updated", ingredient.getLastUpdated());

        return saveOrUpdate(sql, params);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM ingredient WHERE id = ?";
        return deleteById(sql, id);
    }
}
