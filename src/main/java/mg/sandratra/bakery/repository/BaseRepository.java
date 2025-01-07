package mg.sandratra.bakery.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public abstract class BaseRepository<T> {

    protected JdbcTemplate jdbcTemplate;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Constructor to initialize JdbcTemplate and NamedParameterJdbcTemplate
    protected BaseRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * Default RowMapper implementation that uses BeanPropertyRowMapper.
     * It will automatically map columns to properties with the same name.
     * Developers can override this method to provide custom mapping logic.
     */
    protected abstract RowMapper<T> getRowMapper();
    
    // Common CRUD methods

    // Find all rows
    public List<T> findAll(String sql) {
        return jdbcTemplate.query(sql, getRowMapper());
    }

    // Find by ID
    public T findById(String sql, Object id) {
        return jdbcTemplate.queryForObject(sql, getRowMapper(), id);
    }

    // Save or Update (Named Parameters)
    public int saveOrUpdate(String sql, SqlParameterSource params) {
        return namedParameterJdbcTemplate.update(sql, params);
    }

    // Save or Update (Map)
    public int saveOrUpdate(String sql, Map<String, Object> params) {
        return namedParameterJdbcTemplate.update(sql, params);
    }

    // Delete by ID
    public int deleteById(String sql, Object id) {
        return jdbcTemplate.update(sql, id);
    }
}
