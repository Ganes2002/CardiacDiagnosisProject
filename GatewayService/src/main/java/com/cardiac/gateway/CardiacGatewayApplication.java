package com.cardiac.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class CardiacGatewayApplication {

    private static final Logger logger = LoggerFactory.getLogger(CardiacGatewayApplication.class);

    public static void main(String[] args) {
        logger.info("Starting Cardiac Gateway Application...");
        SpringApplication.run(CardiacGatewayApplication.class, args);
        logger.info("Cardiac Gateway Application Started Successfully.");
    }
}
