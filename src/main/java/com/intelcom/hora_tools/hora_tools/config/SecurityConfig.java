package com.intelcom.hora_tools.hora_tools.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("!test")
public class SecurityConfig {

    @Bean
    @ConditionalOnProperty(name = "app.security.authentication.enabled", havingValue = "true")
    public SecurityFilterChain secureFilterChain(HttpSecurity http) {
        try {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/h2-console/**").permitAll()
                            .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/api-docs/**")
                            .permitAll()
                            .requestMatchers("/login/**", "/oauth2/**").permitAll()
                            .anyRequest().authenticated())
                    .oauth2Login(oauth2 -> oauth2
                            .defaultSuccessUrl("/swagger-ui.html", true))
                    .logout(logout -> logout
                            .logoutSuccessUrl("/")
                            .permitAll())
                    .headers(headers -> headers.frameOptions(frame -> frame.disable()));

            return http.build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to configure security", e);
        }
    }

    @Bean
    @ConditionalOnProperty(name = "app.security.authentication.enabled", havingValue = "false", matchIfMissing = true)
    public SecurityFilterChain openFilterChain(HttpSecurity http) {
        try {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll())
                    .headers(headers -> headers.frameOptions(frame -> frame.disable()));

            return http.build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to configure security", e);
        }
    }
}
