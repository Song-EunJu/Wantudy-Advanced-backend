package com.example.wantudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WantudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(WantudyApplication.class, args);
	}


}
