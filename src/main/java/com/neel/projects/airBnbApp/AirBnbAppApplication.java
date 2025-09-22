package com.neel.projects.airBnbApp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirBnbAppApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(AirBnbAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Here");
	}

}
