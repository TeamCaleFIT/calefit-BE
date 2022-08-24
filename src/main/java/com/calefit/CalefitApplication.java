package com.calefit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CalefitApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalefitApplication.class, args);
	}

}
