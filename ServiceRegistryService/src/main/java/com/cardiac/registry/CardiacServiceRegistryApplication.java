package com.cardiac.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class CardiacServiceRegistryApplication {

    private static final Logger logger = LoggerFactory.getLogger(CardiacServiceRegistryApplication.class);

    public static void main(String[] args) {
        logger.info("Starting Cardiac Service Registry Application(Eureka Server)...");
        SpringApplication.run(CardiacServiceRegistryApplication.class, args);
        logger.info("Cardiac Service Registry Application(Eureka Server) Started Successfully.");
    }
}
