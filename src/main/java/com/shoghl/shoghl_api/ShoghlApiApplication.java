package com.shoghl.shoghl_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShoghlApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoghlApiApplication.class, args);
	}

}
