package com.ogong;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class OgongApplication {

	public static void main(String[] args) {
		SpringApplication.run(OgongApplication.class, args);
	}

}
