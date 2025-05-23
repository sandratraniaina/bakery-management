package mg.sandratra.bakery.repository.bmuser;

import mg.sandratra.bakery.models.bmuser.BmUser;
import mg.sandratra.bakery.models.bmuser.Commission;
import mg.sandratra.bakery.repository.BaseRepository;
import mg.sandratra.bakery.utils.filter.Filter;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

@Repository
public class CommissionRepository extends BaseRepository<Commission> {

    public CommissionRepository(DataSource dataSource) {
        super(dataSource);
    }

    public List<Commission> search(Filter filter) {
        String query = filter.buildQuery();
        Map<String, Object> parameters = filter.getParameters();
        return namedParameterJdbcTemplate.query(query, parameters, getRowMapper());
    }

    @Override
    protected RowMapper<Commission> getRowMapper() {
        return (rs, rowNum) -> {
            Commission commission = new Commission();
            BmUser employee = new BmUser();
            
            employee.setId(rs.getLong("user_id"));
            employee.setUserName(rs.getString("username"));
    
            commission.setEmployee(employee);
            commission.setTotalSale(rs.getBigDecimal("total_sale"));
            commission.setTotalCommission(rs.getBigDecimal("total_commission"));
    
            // Sales list can be populated separately if needed, omitted here for simplicity
            commission.setSales(null);
    
            return commission;
        };
    }
}
