package de.ihrigb.fwla.fwlacenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FwlaCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(FwlaCenterApplication.class, args);
	}
}
