package mg.sandratra.bakery.repository.recipe;

import mg.sandratra.bakery.models.recipe.RecipeIngredient;
import mg.sandratra.bakery.repository.BaseRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class RecipeIngredientRepository extends BaseRepository<RecipeIngredient> {

    public RecipeIngredientRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<RecipeIngredient> getRowMapper() {
        return (rs, rowNum) -> {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setRecipeId(rs.getLong("recipe_id"));
            recipeIngredient.setIngredientId(rs.getLong("ingredient_id"));
            recipeIngredient.setQuantity(rs.getBigDecimal("quantity"));
            return recipeIngredient;
        };
    }

    public List<RecipeIngredient> findAll() {
        String sql = "SELECT * FROM recipe_ingredient";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    public RecipeIngredient findById(Long recipeId, Long ingredientId) {
        String sql = "SELECT * FROM recipe_ingredient WHERE recipe_id = ? AND ingredient_id = ?";
        return jdbcTemplate.queryForObject(sql, getRowMapper(), recipeId, ingredientId);
    }
    
    public RecipeIngredient findByRecipeId(Long recipeId) {
        String sql = "SELECT * FROM recipe_ingredient WHERE recipe_id = ?";
        return jdbcTemplate.queryForObject(sql, getRowMapper(), recipeId);
    }

    public int save(RecipeIngredient recipeIngredient) {
        String sql = "INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity) " +
                "VALUES (:recipe_id, :ingredient_id, :quantity)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("recipe_id", recipeIngredient.getRecipeId())
                .addValue("ingredient_id", recipeIngredient.getIngredientId())
                .addValue("quantity", recipeIngredient.getQuantity());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(RecipeIngredient recipeIngredient) {
        String sql = "UPDATE recipe_ingredient SET quantity = :quantity " +
                "WHERE recipe_id = :recipe_id AND ingredient_id = :ingredient_id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("recipe_id", recipeIngredient.getRecipeId())
                .addValue("ingredient_id", recipeIngredient.getIngredientId())
                .addValue("quantity", recipeIngredient.getQuantity());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(Long recipeId, Long ingredientId) {
        String sql = "DELETE FROM recipe_ingredient WHERE recipe_id = ? AND ingredient_id = ?";
        return jdbcTemplate.update(sql, recipeId, ingredientId);
    }
}
