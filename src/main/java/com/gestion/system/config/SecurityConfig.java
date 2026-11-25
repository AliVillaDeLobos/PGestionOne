package com.gestion.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

     @Bean
    public RoleHierarchy roleHierarchy() {
         return RoleHierarchyImpl.withDefaultRolePrefix()
                 .role("ROOT").implies("ADMIN")
                 .role("ADMIN").implies("MANAGER")
                 .role("MANAGER").implies("MEMBER")
                 .build();
     }
}
