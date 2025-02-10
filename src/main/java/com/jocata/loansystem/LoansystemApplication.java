package com.jocata.loansystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;

@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class})
public class LoansystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoansystemApplication.class, args);
	}

}
