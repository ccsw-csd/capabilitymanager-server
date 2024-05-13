package com.ccsw.capabilitymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class CapabilityManagerServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapabilityManagerServerApplication.class, args);
		System.out.println("Arrancamos Capability Manager...");
	}

}
