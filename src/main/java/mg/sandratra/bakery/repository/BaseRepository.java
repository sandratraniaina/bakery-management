package mg.sandratra.bakery.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

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
}
