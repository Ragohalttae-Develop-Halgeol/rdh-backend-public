package com.sfermions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SfermionsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SfermionsBackendApplication.class, args);
	}
}
