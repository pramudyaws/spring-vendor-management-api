package com.spring.vendorAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VendorApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VendorApiApplication.class, args);
		System.out.println("Spring Vendor API is running!");
	}

}
