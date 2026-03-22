package com.yourname.authservice.config;

import com.yourname.authservice.entity.AppUser;
import com.yourname.authservice.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class DefaultAdminSeed {

    @Bean
    CommandLineRunner seedAdminUser(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsername = "admin";
            String adminPassword = "Admin@12345";

            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                String passwordHash = passwordEncoder.encode(adminPassword);
                AppUser admin = new AppUser(UUID.randomUUID(), adminUsername, passwordHash);
                userRepository.save(admin);
            }
        };
    }
}
