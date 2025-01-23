package mg.sandratra.bakery.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class BmUserDetailsService implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;

    public BmUserDetailsService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userQuery = "SELECT id, username, password_hash AS password, true AS enabled FROM bm_user WHERE username = ?";
        String roleQuery = "SELECT username, role AS authority FROM bm_user WHERE username = ?";

        return jdbcTemplate.queryForObject(userQuery, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            String user = rs.getString("username");
            String password = rs.getString("password");
            boolean enabled = rs.getBoolean("enabled");

            List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities = jdbcTemplate.query(
                roleQuery,
                (r, rn) -> new org.springframework.security.core.authority.SimpleGrantedAuthority(r.getString("authority")),
                username // Pass username as a vararg parameter
            );

            return new UserDetails(id, user, password, enabled, authorities);
        }, username); // Pass username as a vararg parameter
    }
}