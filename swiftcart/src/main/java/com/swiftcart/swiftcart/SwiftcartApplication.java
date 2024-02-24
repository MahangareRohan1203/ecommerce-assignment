package com.swiftcart.swiftcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SwiftcartApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwiftcartApplication.class, args);
		System.out.println("<- ...... APPLICATION STARTED ..... ->");
	}

}
