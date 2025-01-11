package mg.sandratra.bakery.repository.sale;

import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import mg.sandratra.bakery.models.sale.SaleDetails;
import mg.sandratra.bakery.repository.BaseRepository;

@Repository
public class SaleDetailsRepository extends BaseRepository<SaleDetails> {

    public SaleDetailsRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<SaleDetails> getRowMapper() {
        return (rs, rowNum) -> {
            SaleDetails saleDetails = new SaleDetails();
            saleDetails.setId(rs.getLong("id"));
            saleDetails.setSaleId(rs.getLong("sale_id"));
            saleDetails.setProductId(rs.getLong("product_id"));
            saleDetails.setQuantity(rs.getInt("quantity"));
            saleDetails.setUnitPrice(rs.getBigDecimal("unit_price"));
            saleDetails.setSubTotal(rs.getBigDecimal("subtotal"));
            saleDetails.setCreatedAt(rs.getTimestamp("created_at"));
            return saleDetails;
        };
    }

    public List<SaleDetails> findAll() {
        String sql = "SELECT * FROM sale_details";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    public SaleDetails findById(Long id) {
        String sql = "SELECT * FROM sale_details WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, getRowMapper(), id);
    }

    // Save method: Inserts a new sale details record
    public int save(SaleDetails saleDetails) {
        String sql = "INSERT INTO sale_details (sale_id, product_id, quantity, unit_price) "
                + "VALUES (:sale_id, :product_id, :quantity, :unit_price)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("sale_id", saleDetails.getSaleId())
                .addValue("product_id", saleDetails.getProductId())
                .addValue("quantity", saleDetails.getQuantity())
                .addValue("unit_price", saleDetails.getUnitPrice());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    // Update method: Updates an existing sale details record
    public int update(SaleDetails saleDetails) {
        String sql = "UPDATE sale_details SET " +
                "sale_id = :sale_id, product_id = :product_id, quantity = :quantity, " +
                "unit_price = :unit_price, created_at = :created_at " +
                "WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", saleDetails.getId())
                .addValue("sale_id", saleDetails.getSaleId())
                .addValue("product_id", saleDetails.getProductId())
                .addValue("quantity", saleDetails.getQuantity())
                .addValue("unit_price", saleDetails.getUnitPrice())
                .addValue("created_at", Timestamp.valueOf(saleDetails.getCreatedAt().toLocalDateTime()));

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM sale_details WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
