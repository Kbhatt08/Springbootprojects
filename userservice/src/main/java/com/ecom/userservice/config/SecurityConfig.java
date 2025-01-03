package com.ecom.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs (optional)
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/register", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**", "/v3/api-docs/**",    // Allow access to OpenAPI docs
                                        "/swagger-ui/**",     // Allow access to Swagger UI
                                        "/swagger-ui.html",
                                        "/api/v1/users").permitAll() // Allow public access to Swagger UI and related resources
                                .anyRequest().authenticated() // Protect all other endpoints
                )
                .httpBasic(); // Enable Basic Authentication (username and password)
        
        return http.build();
    }

    // Define an in-memory user with username and password
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("user") // Replace with your desired username
                .password("{noop}pass") // Replace with your desired password
                .roles("USER") // You can assign roles as needed
                .build()
        );
    }
}
