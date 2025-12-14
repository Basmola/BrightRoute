package com.brightroute.brightroute.config;

import com.brightroute.brightroute.enums.AccountStatus;
import com.brightroute.brightroute.enums.Role;
import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@brightroute.com";

            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = new User();
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setEmail(adminEmail);
                admin.setPhoneNumber("0000000000");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                admin.setAccountStatus(AccountStatus.ACTIVE);

                userRepository.save(admin);
                System.out.println("✅ ADMIN user created: " + adminEmail);
            } else {
                System.out.println("ℹ️ ADMIN user already exists.");
            }
        };
    }
}
