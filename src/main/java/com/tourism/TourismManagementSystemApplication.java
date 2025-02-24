package com.tourism;

import com.tourism.model.domain.Tour;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TourismManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourismManagementSystemApplication.class, args);

		new Tour();
	}

}
