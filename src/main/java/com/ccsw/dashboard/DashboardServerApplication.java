package com.ccsw.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class DashboardServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashboardServerApplication.class, args);
		System.out.println("arrancamos dashboard...");
	}

}
