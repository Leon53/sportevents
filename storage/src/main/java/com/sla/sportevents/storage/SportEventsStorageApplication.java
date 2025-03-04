package com.sla.sportevents.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
		"com.sla.sportevents.db.repository"
})
@ComponentScan(basePackages = {
		"com.sla.sportevents"
})
@EntityScan({
		"com.sla.sportevents.db",
		"com.sla.sportevents.db.repository",
})
@TestPropertySource(locations = "classpath:test-application.properties")
public class SportEventsStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportEventsStorageApplication.class, args);
	}

}
