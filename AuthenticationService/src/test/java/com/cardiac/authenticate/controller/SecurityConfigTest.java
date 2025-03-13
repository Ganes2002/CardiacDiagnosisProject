package com.cardiac.authenticate.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class SecurityConfigTest {
	  @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	        httpSecurity
	            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Disable authentication
	            .csrf(csrf -> csrf.disable()) // Disable CSRF (important for POST requests)
	            .formLogin(form -> form.disable()) // Disable form login
	            .httpBasic(basic -> basic.disable()); // Disable HTTP Basic Auth
	        return httpSecurity.build();
	    }
}