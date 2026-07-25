package com.adinath.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.adinath.entity.Admin;
import com.adinath.repository.AdminRepository;

import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
public class DataInitializer{

    @Bean
    CommandLineRunner initAdmin(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (adminRepository.count() == 0) {

                Admin admin = new Admin();

                admin.setFullName("Harsh Sharma");
                admin.setUsername("admin");
                admin.setPassword(
                        passwordEncoder.encode("admin123"));
                admin.setEmail("adinathclinic14@gmail.com");
                admin.setMobileNumber("9588900642");

                adminRepository.save(admin);

            }

        };

    }

}