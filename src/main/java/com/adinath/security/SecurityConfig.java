package com.adinath.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(
            CustomUserDetailsService customUserDetailsService) {

        this.customUserDetailsService = customUserDetailsService;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

                .userDetailsService(customUserDetailsService)

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/about",
                                "/doctors",
                                "/appointment/**",
                                "/error",
                                "/contact/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.svg")
                        .permitAll()

                        .requestMatchers("/admin/login")
                        .permitAll()

                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        .anyRequest()
                        .permitAll())

                .formLogin(login -> login

                        .loginPage("/admin/login")

                        .loginProcessingUrl("/admin/login")

                        .defaultSuccessUrl("/admin/dashboard", true)

                        .failureUrl("/admin/login?error")

                        .permitAll())

                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/admin/login?logout")

                        .invalidateHttpSession(true)

                        .clearAuthentication(true)

                        .deleteCookies("JSESSIONID")

                        .permitAll());

        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();

    }

}