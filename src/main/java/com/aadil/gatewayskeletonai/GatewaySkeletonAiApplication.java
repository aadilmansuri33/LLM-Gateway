package com.aadil.gatewayskeletonai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewaySkeletonAiApplication {
    private static final Logger log = LoggerFactory.getLogger(GatewaySkeletonAiApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(GatewaySkeletonAiApplication.class, args);
        log.info("LLM Gateway started (Java {})", System.getProperty("java.version"));
    }
}
