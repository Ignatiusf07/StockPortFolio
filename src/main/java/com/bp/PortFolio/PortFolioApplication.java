package com.bp.PortFolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PortFolioApplication {
    public static void main(String[] args) {
        SpringApplication.run(PortFolioApplication.class, args);
    }
}