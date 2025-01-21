package mg.sandratra.bakery.repository.bmuser;

import mg.sandratra.bakery.models.bmuser.BmUser;
import mg.sandratra.bakery.models.bmuser.Commission;
import mg.sandratra.bakery.repository.BaseRepository;
import mg.sandratra.bakery.services.bmuser.filter.CommissionFilter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.sql.DataSource;

@Repository
public class CommissionRepository extends BaseRepository<Commission> {

    public CommissionRepository(DataSource dataSource) {
        super(dataSource);
    }

    public List<Commission> search(CommissionFilter filter) {
        String query = filter.buildQuery();

        return jdbcTemplate.query(query, getRowMapper());
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
