package mg.sandratra.bakery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (not recommended for production)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()) // Require authentication for all requests
            .formLogin(form -> form
                .loginPage("/login") // Set the custom login page
                .permitAll()) // Allow everyone to access the login page
            .logout(LogoutConfigurer::permitAll); // Allow all to log out

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Define SQL queries to retrieve user and role
        userDetailsManager.setUsersByUsernameQuery(
            "SELECT username, password_hash AS password, true AS enabled FROM bm_user WHERE username = ?"
        );
        userDetailsManager.setAuthoritiesByUsernameQuery(
            "SELECT username, role AS authority FROM bm_user WHERE username = ?"
        );

        return userDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for password hashing
    }
}