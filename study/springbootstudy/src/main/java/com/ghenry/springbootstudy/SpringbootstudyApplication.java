package com.ghenry.springbootstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringbootstudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootstudyApplication.class, args);
	}

	@RequestMapping("/hello")
	public String hello(){
		System.out.println("hello");
		return "hello";
	}
}
