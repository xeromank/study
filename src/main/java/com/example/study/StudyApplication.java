package com.example.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class StudyApplication {

	public static final String APPLICATION_PROPS_LOCATIONS = "spring.config.location="
        + "classpath:application.yml";

	public static void main(String[] args) {
//		SpringApplication.run(StudyApplication.class, args);
		new SpringApplicationBuilder(StudyApplication.class).properties(
            APPLICATION_PROPS_LOCATIONS).run(args);
	}

}
