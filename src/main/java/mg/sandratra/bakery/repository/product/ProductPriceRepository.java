package mg.sandratra.bakery.repository.product;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import mg.sandratra.bakery.models.product.ProductPriceHistory;
import mg.sandratra.bakery.repository.BaseRepository;
import mg.sandratra.bakery.services.product.filter.ProductPriceHistoryFilter;

@Repository
public class ProductPriceRepository extends BaseRepository<ProductPriceHistory> {

    public ProductPriceRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<ProductPriceHistory> getRowMapper() {
        return (rs, rowNum) -> {
            ProductPriceHistory productPrice = new ProductPriceHistory();
            productPrice.setId(rs.getLong("id"));

            productPrice.setProductId(rs.getLong("product_id"));

            productPrice.setValue(rs.getBigDecimal("value"));
            productPrice.setPriceDate(rs.getTimestamp("price_date"));
            return productPrice;
        };
    }

    public List<ProductPriceHistory> filterProductPrices(ProductPriceHistoryFilter filter) {
        String sql = filter.buildQuery();
        Map<String, Object> parameters = filter.getParameters();
        return namedParameterJdbcTemplate.query(sql, parameters, getRowMapper());
    }

    public List<ProductPriceHistory> findAll() {
        String sql = "SELECT * FROM product_price_history";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    public ProductPriceHistory findById(Long id) {
        String sql = "SELECT * FROM product_price_history WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, getRowMapper(), id);
    }

    public List<ProductPriceHistory> findByProductId(Long productId) {
        String sql = "SELECT * FROM product_price_history WHERE product_id = ?";
        return jdbcTemplate.query(sql, getRowMapper(), productId);
    }

    public int save(ProductPriceHistory productPrice) {
        String sql = "INSERT INTO product_price_history (product_id, value, price_date) " +
                "VALUES (:product_id, :value, :price_date)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("product_id", productPrice.getProductId())
                .addValue("value", productPrice.getValue())
                .addValue("price_date", productPrice.getPriceDate());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(ProductPriceHistory productPrice) {
        String sql = "UPDATE product_price_history SET " +
                "product_id = :product_id, value = :value, price_date = :price_date " +
                "WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", productPrice.getId())
                .addValue("product_id", productPrice.getProductId())
                .addValue("value", productPrice.getValue())
                .addValue("price_date", productPrice.getPriceDate());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM product_price_history WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}