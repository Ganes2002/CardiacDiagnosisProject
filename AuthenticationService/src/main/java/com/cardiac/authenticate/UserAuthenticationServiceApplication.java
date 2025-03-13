package com.cardiac.authenticate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin(origins = "*")
@SpringBootApplication
@EnableWebSecurity
public class UserAuthenticationServiceApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(UserAuthenticationServiceApplication.class, args);
		
	}

}