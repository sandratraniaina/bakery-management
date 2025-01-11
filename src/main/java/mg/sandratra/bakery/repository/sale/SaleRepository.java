package mg.sandratra.bakery.repository.sale;

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

import mg.sandratra.bakery.models.sale.Sale;
import mg.sandratra.bakery.repository.BaseRepository;

@Repository
public class SaleRepository extends BaseRepository<Sale> {

    public SaleRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<Sale> getRowMapper() {
        return new RowMapper<Sale>() {
            @Override
            public Sale mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                Sale sale = new Sale();
                sale.setId(rs.getLong("id"));
                sale.setCreatedBy(rs.getLong("created_by"));
                sale.setClientName(rs.getString("client_name"));
                sale.setTotalAmount(rs.getBigDecimal("total_amount"));
                sale.setSaleDate(rs.getDate("sale_date"));
                sale.setCreatedAt(rs.getTimestamp("created_at"));
                return sale;
            }
        };
    }

    public List<Sale> findAll() {
        String sql = "SELECT * FROM sale";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    public Sale findById(Long id) {
        String sql = "SELECT * FROM sale WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, getRowMapper(), id);
    }

    public Long save(Sale sale) {
        String sql = "INSERT INTO sale (created_by, client_name, total_amount, sale_date) "
                + "VALUES (:created_by, :client_name, :total_amount, :sale_date)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("created_by", sale.getCreatedBy())
                .addValue("client_name", sale.getClientName())
                .addValue("total_amount", sale.getTotalAmount())
                .addValue("sale_date", sale.getSaleDate());

        KeyHolder holder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, params, holder, new String[] { "id" });

        return holder.getKey().longValue();
    }

    public int update(Sale sale) {
        String sql = "UPDATE sale SET " +
                "created_by = :created_by, client_name = :client_name, " +
                "total_amount = :total_amount, sale_date = :sale_date, " +
                "created_at = :created_at " +
                "WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", sale.getId())
                .addValue("created_by", sale.getCreatedBy())
                .addValue("client_name", sale.getClientName())
                .addValue("total_amount", sale.getTotalAmount())
                .addValue("sale_date", sale.getSaleDate())
                .addValue("created_at", Timestamp.valueOf(LocalDateTime.now()));

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM sale WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
