package com.bookmart.bookmart_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class BookmartBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookmartBackendApplication.class, args);
	}

}
