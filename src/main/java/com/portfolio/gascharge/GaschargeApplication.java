package com.portfolio.gascharge;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class GaschargeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GaschargeApplication.class, args);
	}
}
