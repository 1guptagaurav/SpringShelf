package com.Library.SpringShelf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringShelfApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringShelfApplication.class, args);
	}
}