package com.devsenior.atiende;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.devsenior.atiende.ia.IaProperties;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.devsenior.atiende")
public class AtiendeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtiendeApplication.class, args);
	}

	@Bean
	RestClient.Builder restClientBuilder(IaProperties iaProperties) {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(iaProperties.getTimeoutSegundos() * 1000);
		requestFactory.setReadTimeout(iaProperties.getTimeoutSegundos() * 1000);
		return RestClient.builder().requestFactory(requestFactory);
	}
}
