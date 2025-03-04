package com.sla.sportevents.db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootConfiguration
@EnableAutoConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
@PropertySource("classpath:test-application.properties")
@ComponentScan(basePackages = {"com.sla.sportevents.db"})
@ActiveProfiles("test")
public class SportEventTestConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(SportEventTestConfiguration.class, args);
    }
}