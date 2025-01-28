package mg.sandratra.bakery.repository.bmuser;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import mg.sandratra.bakery.enums.Role;
import mg.sandratra.bakery.models.bmuser.BmUser;
import mg.sandratra.bakery.models.bmuser.Gender;
import mg.sandratra.bakery.repository.BaseRepository;

@Repository
public class BmUserRepository extends BaseRepository<BmUser> {

    public BmUserRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RowMapper<BmUser> getRowMapper() {
        return (rs, rowNum) -> {
            BmUser bmUser = new BmUser();

            Gender gender = new Gender();

            gender.setId(rs.getLong("gender_id"));
            gender.setName(rs.getString("gender_name"));

            bmUser.setId(rs.getLong("user_id"));
            bmUser.setUserName(rs.getString("username"));
            bmUser.setPasswordHash(rs.getString("password_hash"));
            bmUser.setRole(Role.valueOf(rs.getString("role").toUpperCase()));
            bmUser.setEnabled(rs.getBoolean("enabled"));
            bmUser.setCreatedAt(rs.getTimestamp("user_created_at"));
            return bmUser;
        };
    }

    public List<BmUser> findAll() {
        String sql = "SELECT * FROM v_user_gender";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    public BmUser findById(Long id) {
        String sql = "SELECT * FROM v_user_gender WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, getRowMapper(), id);
    }

    public int save(BmUser bmUser) {
        String sql = "INSERT INTO bm_user (username, password_hash, role, enabled) " +
                     "VALUES (:username, :password_hash, CAST(:role AS role), :enabled)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", bmUser.getUserName())
                .addValue("password_hash", bmUser.getEncodedPassword())
                .addValue("role", bmUser.getRole().toString())
                .addValue("enabled", bmUser.getEnabled());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(BmUser bmUser) {
        String sql = "UPDATE bm_user SET " +
                     "username = :username, " +
                     "password_hash = :password_hash, " +
                     "role = CAST(:role AS role), " +
                     "enabled = :enabled " +
                     "WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", bmUser.getId())
                .addValue("username", bmUser.getUserName())
                .addValue("password_hash", bmUser.getEncodedPassword())
                .addValue("role", bmUser.getRole().toString())
                .addValue("enabled", bmUser.getEnabled());

        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM bm_user WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
