package com.gestion.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {


    @Bean
    PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder(14,new SecureRandom());}


    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ROOT").implies("ADMIN")
                .role("ADMIN").implies("MANAGER")
                .role("MANAGER").implies("MEMBER")
                .build();
    }
}
