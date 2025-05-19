package mg.sandratra.bakery.repository.bmuser;

import java.math.BigDecimal;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mg.sandratra.bakery.models.bmuser.Commission;
import mg.sandratra.bakery.models.bmuser.CommissionSummary;
import mg.sandratra.bakery.models.bmuser.Gender;

@Repository
public class CommissionSummaryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CommissionSummaryRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    // RowMapper to map the result set to CommissionSummary
    private RowMapper<CommissionSummary> getRowMapper() {
        return (rs, rowNum) -> {
            Gender gender = new Gender();
            gender.setId(rs.getLong("gender_id"));
            gender.setName(rs.getString("gender_name"));

            BigDecimal totalCommission = rs.getBigDecimal("total_commission");

            return new CommissionSummary(gender, totalCommission);
        };
    }

    // Method to get commission summaries by gender ID (or all if genderId is null)
    public List<CommissionSummary> getCommissionSummaries(Long genderId) {
        String sql = """
            SELECT 
                g.id AS gender_id, 
                g.name AS gender_name, 
                COALESCE(SUM(s.total_amount * 0.05), 0) AS total_commission
            FROM 
                gender g
            LEFT JOIN 
                bm_user u ON g.id = u.gender_id
            LEFT JOIN 
                sale s ON u.id = s.created_by
            WHERE 
                (CAST(:genderId AS BIGINT) IS NULL OR g.id = CAST(:genderId AS BIGINT))
                AND (s.total_amount IS NULL OR s.total_amount > :threeshold)
            GROUP BY 
                g.id, g.name
            ORDER BY 
                g.id
            """;
    
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("genderId", genderId)
                .addValue("threeshold", Commission.COMMISSION_THREESHOLD);
    
        return jdbcTemplate.query(sql, params, getRowMapper());
    }
}