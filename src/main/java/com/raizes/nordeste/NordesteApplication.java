package com.raizes.nordeste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NordesteApplication {

	public static void main(String[] args) {
		SpringApplication.run(NordesteApplication.class, args);
	}

}
