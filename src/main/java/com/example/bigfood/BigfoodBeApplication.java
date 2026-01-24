package com.example.bigfood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@EntityScan(basePackages = "com.example.bigfood") 
public class BigfoodBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BigfoodBeApplication.class, args);
	}

}
