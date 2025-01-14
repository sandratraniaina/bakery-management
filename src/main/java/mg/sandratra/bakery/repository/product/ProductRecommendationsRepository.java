package mg.sandratra.bakery.repository.product;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import mg.sandratra.bakery.models.product.ProductRecommendations;
import mg.sandratra.bakery.repository.BaseRepository;
import mg.sandratra.bakery.utils.filter.Filter;

@Repository
public class ProductRecommendationsRepository extends BaseRepository<ProductRecommendations> {

    public ProductRecommendationsRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<ProductRecommendations> getRowMapper() {
        return (rs, rowNum) -> {
            ProductRecommendations recommendation = new ProductRecommendations();
            recommendation.setId(rs.getLong("id"));
            recommendation.setProductId(rs.getLong("product_id"));
            recommendation.setCreatedAt(rs.getDate("created_at"));
            return recommendation;
        };
    }

    public List<ProductRecommendations> findAll() {
        String sql = "SELECT * FROM product_recommendations";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    public List<ProductRecommendations> search(Filter filter) {
        String sql = filter.buildQuery();
        return jdbcTemplate.query(sql, getRowMapper());
    }

    public ProductRecommendations findById(Long id) {
        String sql = "SELECT * FROM product_recommendations WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, getRowMapper(), id);
    }

    public int save(ProductRecommendations recommendation) {
        String sql = "INSERT INTO product_recommendations (product_id, created_at) " +
                "VALUES (:product_id, :created_at)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("product_id", recommendation.getProductId())
                .addValue("created_at", recommendation.getCreatedAt());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(ProductRecommendations recommendation) {
        String sql = "UPDATE product_recommendations SET " +
                "product_id = :product_id, created_at = :created_at " +
                "WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", recommendation.getId())
                .addValue("product_id", recommendation.getProductId())
                .addValue("created_at", recommendation.getCreatedAt());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM product_recommendations WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
