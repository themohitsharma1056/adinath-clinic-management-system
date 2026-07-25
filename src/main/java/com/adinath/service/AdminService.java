package com.adinath.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.adinath.entity.Admin;
import com.adinath.repository.AdminRepository;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminService(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {

        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public Optional<Admin> findByUsername(
            String username) {

        return adminRepository.findByUsername(username);

    }

    public Admin save(Admin admin) {

        return adminRepository.save(admin);

    }

    public boolean updateUsername(

            String currentUsername,

            String newUsername) {

        Optional<Admin> optional =
                adminRepository.findByUsername(currentUsername);

        if (optional.isEmpty()) {

            return false;

        }

        if (adminRepository.findByUsername(newUsername).isPresent()) {

            return false;

        }

        Admin admin = optional.get();

        admin.setUsername(newUsername);

        adminRepository.save(admin);

        return true;

    }

    public boolean updatePassword(

            String username,

            String currentPassword,

            String newPassword) {

        Optional<Admin> optional =
                adminRepository.findByUsername(username);

        if (optional.isEmpty()) {

            return false;

        }

        Admin admin = optional.get();

        if (!passwordEncoder.matches(
                currentPassword,
                admin.getPassword())) {

            return false;

        }

        admin.setPassword(

                passwordEncoder.encode(newPassword));

        adminRepository.save(admin);

        return true;

    }

}