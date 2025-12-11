// File: com.brightroute.brightroute.config.SecurityConfig.java

package com.brightroute.brightroute.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    /**
     * Defines the BCryptPasswordEncoder bean for password hashing.
     * This bean is then automatically injected into the UserService.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}