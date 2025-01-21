package mg.sandratra.bakery.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;

    public CustomUserDetailsService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userQuery = "SELECT id, username, password_hash AS password, true AS enabled FROM bm_user WHERE username = ?";
        String roleQuery = "SELECT username, role AS authority FROM bm_user WHERE username = ?";

        return jdbcTemplate.queryForObject(userQuery, new Object[]{username}, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            String user = rs.getString("username");
            String password = rs.getString("password");
            boolean enabled = rs.getBoolean("enabled");

            var authorities = jdbcTemplate.query(roleQuery, new Object[]{username}, (r, rn) ->
                new org.springframework.security.core.authority.SimpleGrantedAuthority(r.getString("authority"))
            );

            return new CustomUserDetails(id, user, password, authorities);
        });
    }
}
