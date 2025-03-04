package com.sla.sportevents.retrieval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
public class SportEventsRetrievalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportEventsRetrievalApplication.class, args);
	}

}
