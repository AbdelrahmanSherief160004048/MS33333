package com.example.ms3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        // 1. Allow Login, Signup, Static files
                        .requestMatchers("/login", "/signup", "/css/**", "/js/**", "/images/**", "/error").permitAll()

                        // 2. Allow API Endpoints
                        .requestMatchers("/api/login", "/api/employees").permitAll()

                        // 3. Allow POST requests
                        .requestMatchers(HttpMethod.POST, "/signup", "/api/login").permitAll()

                        // 4. Allow Dashboards & specific APIs
                        .requestMatchers(
                                "/hr-dashboard",
                                "/manager-dashboard",
                                "/sysadmin-dashboard",
                                "/payroll-dashboard",
                                "/employee-dashboard",
                                "/employees",
                                "/api/employees/update",
                                "/api/employees/upload-photo",
                                "/manager/team",
                                "/api/manager/**",
                                "/profile",
                                "/api/employees/**",
                                "/api/contracts/**",
                                "/api/leaves/**",
                                "/uploads/**",
                                "/api/shifts/**"
                        ).permitAll()

                        // 5. Block everything else
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}