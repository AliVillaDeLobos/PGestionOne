package com.gestion.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Configuration
public class SecurityConfig {


    @Bean
    PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder(14,new SecureRandom());}
}
