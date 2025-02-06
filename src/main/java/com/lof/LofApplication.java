package com.lof;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class LofApplication {

	public static void main(String[] args) {
		SpringApplication.run(LofApplication.class, args);
	}

}
