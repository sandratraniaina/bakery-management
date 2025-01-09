package mg.sandratra.bakery.repository.breadmaking;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import mg.sandratra.bakery.models.breadmaking.Breadmaking;
import mg.sandratra.bakery.repository.BaseRepository;

@Repository
public class BreadmakingRepository extends BaseRepository<Breadmaking> {

    public BreadmakingRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<Breadmaking> getRowMapper() {
        return (rs, rowNum) -> {
            Breadmaking breadmaking = new Breadmaking();
            breadmaking.setId(rs.getLong("id"));
            breadmaking.setProductId(rs.getLong("product_id"));
            breadmaking.setCreatedBy(rs.getLong("created_by"));
            breadmaking.setQuantity(rs.getLong("quantity"));
            breadmaking.setProductionDate(rs.getDate("production_date"));
            breadmaking.setIngredientCost(rs.getBigDecimal("ingredient_cost"));
            breadmaking.setOtherCost(rs.getBigDecimal("other_cost"));
            breadmaking.setCostPerUnit(rs.getBigDecimal("cost_per_unit"));
            return breadmaking;
        };
    }

    public List<Breadmaking> findAll() {
        String sql = "SELECT * FROM breadmaking";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    public Breadmaking findById(Long id) {
        String sql = "SELECT * FROM breadmaking WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, getRowMapper(), id);
    }

    public List<Breadmaking> findByIngredientId(Long ingredientId) {
        String sql = "SELECT * FROM v_breadmaking_ingredients WHERE ingredient_id = ?";
        return jdbcTemplate.query(sql, getRowMapper(), ingredientId);
    }

    // Save method: Inserts a new breadmaking record
    public int save(Breadmaking breadmaking) {
        String sql = "INSERT INTO breadmaking (product_id, created_by, quantity, production_date, ingredient_cost, other_cost) " +
                "VALUES (:product_id, :created_by, :quantity, :production_date, :ingredient_cost, :other_cost)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("product_id", breadmaking.getProductId())
                .addValue("created_by", breadmaking.getCreatedBy())
                .addValue("quantity", breadmaking.getQuantity())
                .addValue("production_date", breadmaking.getProductionDate())
                .addValue("ingredient_cost", breadmaking.getIngredientCost())
                .addValue("other_cost", breadmaking.getOtherCost());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    // Update method: Updates an existing breadmaking record
    public int update(Breadmaking breadmaking) {
        String sql = "UPDATE breadmaking SET " +
                "product_id = :product_id, created_by = :created_by, quantity = :quantity, " +
                "production_date = :production_date, ingredient_cost = :ingredient_cost, " +
                "other_cost = :other_cost, created_at = :created_at " +
                "WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", breadmaking.getId())
                .addValue("product_id", breadmaking.getProductId())
                .addValue("created_by", breadmaking.getCreatedBy())
                .addValue("quantity", breadmaking.getQuantity())
                .addValue("production_date", breadmaking.getProductionDate())
                .addValue("ingredient_cost", breadmaking.getIngredientCost())
                .addValue("other_cost", breadmaking.getOtherCost())
                .addValue("created_at", Timestamp.valueOf(LocalDateTime.now()));

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM breadmaking WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
