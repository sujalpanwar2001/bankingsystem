package com.nagarro.customermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CustomermanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomermanagementApplication.class, args);
	}

}
