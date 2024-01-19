package com.samplecompany.computer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.samplecompany.computer")
@EntityScan(basePackages = "com.samplecompany.computer.dao")
@ComponentScan("com.samplecompany.computer")
public class ComputerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ComputerApplication.class, args);
	}
}
