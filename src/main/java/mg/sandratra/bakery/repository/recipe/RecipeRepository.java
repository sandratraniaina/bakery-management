package mg.sandratra.bakery.repository.recipe;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mg.sandratra.bakery.models.recipe.Recipe;
import mg.sandratra.bakery.repository.BaseRepository;

@Repository
public class RecipeRepository extends BaseRepository<Recipe> {

    public RecipeRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<Recipe> getRowMapper() {
        return (rs, rowNum) -> {
            Recipe recipe = new Recipe();
            recipe.setId(rs.getLong("id"));
            recipe.setName(rs.getString("name"));
            recipe.setDescription(rs.getString("description"));
            recipe.setCreatedAt(rs.getTimestamp("created_at"));
            return recipe;
        };
    }

    public List<Recipe> findAll() {
        String sql = "SELECT * FROM recipe";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    public Recipe findById(Long id) {
        String sql = "SELECT * FROM recipe WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, getRowMapper(), id);
    }

    public Long save(Recipe recipe) {
        String sql = "INSERT INTO recipe (name, description, created_at) " +
                "VALUES (:name, :description, :created_at)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", recipe.getName())
                .addValue("description", recipe.getDescription())
                .addValue("created_at",
                        recipe.getCreatedAt() != null ? recipe.getCreatedAt() : Timestamp.valueOf(LocalDateTime.now()));

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[] { "id" });

        return keyHolder.getKey().longValue();
    }

    public int update(Recipe recipe) {
        String sql = "UPDATE recipe SET " +
                "name = :name, description = :description, created_at = :created_at " +
                "WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", recipe.getId())
                .addValue("name", recipe.getName())
                .addValue("description", recipe.getDescription())
                .addValue("created_at",
                        recipe.getCreatedAt() != null ? recipe.getCreatedAt() : Timestamp.valueOf(LocalDateTime.now()));

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM recipe WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
