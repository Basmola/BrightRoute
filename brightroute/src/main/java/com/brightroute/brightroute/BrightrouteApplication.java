package com.brightroute.brightroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.cloud.openfeign.EnableFeignClients
public class BrightrouteApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrightrouteApplication.class, args);
	}

}