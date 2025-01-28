package mg.sandratra.bakery.repository.bmuser;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import mg.sandratra.bakery.models.bmuser.Gender;
import mg.sandratra.bakery.repository.BaseRepository;

@Repository
public class GenderRepository extends BaseRepository<Gender> {

    public GenderRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<Gender> getRowMapper() {
        return (rs, rowNum) -> {
            Gender gender = new Gender();
            gender.setId(rs.getLong("id"));
            gender.setName(rs.getString("name"));
            return gender;
        };
    }

    public List<Gender> findAll() {
        String sql = "SELECT * FROM gender";
        return jdbcTemplate.query(sql, getRowMapper());
    }
}