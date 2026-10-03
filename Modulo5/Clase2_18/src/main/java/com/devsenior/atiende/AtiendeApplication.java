package com.devsenior.atiende;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AtiendeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtiendeApplication.class, args);
	}

}
