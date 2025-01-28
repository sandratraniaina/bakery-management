package mg.sandratra.bakery.repository.product.history;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import mg.sandratra.bakery.models.product.Product;
import mg.sandratra.bakery.models.product.history.ProductPrice;
import mg.sandratra.bakery.repository.BaseRepository;

@Repository
public class ProductPriceRepository extends BaseRepository<ProductPrice> {

    public ProductPriceRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<ProductPrice> getRowMapper() {
        return (rs, rowNum) -> {
            ProductPrice productPrice = new ProductPrice();
            productPrice.setId(rs.getLong("id"));
            
            Product product = new Product();
            product.setId(rs.getLong("product_id"));
            productPrice.setProduct(product);
            
            productPrice.setValue(rs.getBigDecimal("value"));
            productPrice.setPriceDate(rs.getTimestamp("price_date"));
            return productPrice;
        };
    }

    public List<ProductPrice> findAll() {
        String sql = "SELECT * FROM product_price_history";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    public ProductPrice findById(Long id) {
        String sql = "SELECT * FROM product_price_history WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, getRowMapper(), id);
    }

    public List<ProductPrice> findByProductId(Long productId) {
        String sql = "SELECT * FROM product_price_history WHERE product_id = ?";
        return jdbcTemplate.query(sql, getRowMapper(), productId);
    }

    public int save(ProductPrice productPrice) {
        String sql = "INSERT INTO product_price_history (product_id, value, price_date) " +
                "VALUES (:product_id, :value, :price_date)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("product_id", productPrice.getProduct().getId())
                .addValue("value", productPrice.getValue())
                .addValue("price_date", productPrice.getPriceDate());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(ProductPrice productPrice) {
        String sql = "UPDATE product_price_history SET " +
                "product_id = :product_id, value = :value, price_date = :price_date " +
                "WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", productPrice.getId())
                .addValue("product_id", productPrice.getProduct().getId())
                .addValue("value", productPrice.getValue())
                .addValue("price_date", productPrice.getPriceDate());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM product_price_history WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}