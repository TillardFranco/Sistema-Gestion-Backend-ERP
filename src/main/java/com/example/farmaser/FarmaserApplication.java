package com.example.farmaser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication

// este enable scheduling es para que el scheduler se ejecute cada hora
@EnableScheduling
public class FarmaserApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmaserApplication.class, args);
	}

}
