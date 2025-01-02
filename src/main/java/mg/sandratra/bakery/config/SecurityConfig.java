package mg.sandratra.bakery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable Cross-Site Request Forgery (CSRF) for simplicity
            .csrf().disable()
            
            // Allow all requests without requiring authentication
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            
            // Disable form-based login (no redirect to login page)
            .formLogin().disable()
            
            // Disable HTTP Basic authentication (optional)
            .httpBasic().disable();
        
        return http.build();
    }
}
