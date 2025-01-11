package mg.sandratra.bakery.repository.product;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import mg.sandratra.bakery.enums.ProductType;
import mg.sandratra.bakery.models.product.Product;
import mg.sandratra.bakery.repository.BaseRepository;

@Repository
public class ProductRepository extends BaseRepository<Product> {

    public ProductRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<Product> getRowMapper() {
        return (rs, rowNum) -> {
            Product product = new Product();
            product.setId(rs.getLong("id"));
            product.setRecipeId(rs.getLong("recipe_id"));
            product.setName(rs.getString("name"));
            product.setProductType(ProductType.valueOf(rs.getString("product_type")));
            product.setPrice(rs.getBigDecimal("price"));
            product.setStockQuantity(rs.getLong("stock_quantity"));
            product.setCreatedAt(rs.getTimestamp("created_at"));
            return product;
        };
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    public Product findById(Long id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, getRowMapper(), id);
    }

    public int save(Product product) {
        String sql = "INSERT INTO product (recipe_id, name, price, stock_quantity, product_type) " +
                "VALUES (:recipe_id, :name, :price, :stock_quantity, CAST(:product_type AS product_type))";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("recipe_id", product.getRecipeId())
                .addValue("name", product.getName())
                .addValue("price", product.getPrice())
                .addValue("stock_quantity", product.getStockQuantity())
                .addValue("product_type", product.getProductType().toString());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(Product product) {
        String sql = "UPDATE product SET " +
                "recipe_id = :recipe_id, name = :name, price = :price, " +
                "stock_quantity = :stock_quantity, product_type = CAST(:product_type AS product_type)" +
                "WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", product.getId())
                .addValue("recipe_id", product.getRecipeId())
                .addValue("name", product.getName())
                .addValue("price", product.getPrice())
                .addValue("stock_quantity", product.getStockQuantity())
                .addValue("product_type", product.getProductType().toString());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM product WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
