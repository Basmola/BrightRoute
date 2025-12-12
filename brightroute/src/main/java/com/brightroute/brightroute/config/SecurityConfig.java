package com.brightroute.brightroute.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Still needed for explicit method matching, though the broad rule will cover it.
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines the security filter chain which sets up authorization rules.
     * This version is edited to grant public access to ALL endpoints under /api/**
     * to prevent 401/403 errors across all controllers.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disables CSRF, preventing 403 Forbidden errors on POST/PUT requests
            .csrf(csrf -> csrf.disable()) 
            
            // Configures Authorization Rules
            .authorizeHttpRequests(auth -> auth
                
                // 🚀 GLOBAL FIX: Permit ALL traffic to ALL controllers under /api/
                // This replaces the separate rules for users and courses.
                // This ensures that all your controllers (Lectures, Quizzes, Submissions, etc.) 
                // are publicly accessible for development/testing.
                .requestMatchers("/api/**").permitAll() 
                
                // Allow OPTIONS requests (important for frontend pre-flight checks)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Fallback: This is now just a safety net since /api/** is permitted.
                .anyRequest().permitAll() 
            );

        return http.build();
    }
}