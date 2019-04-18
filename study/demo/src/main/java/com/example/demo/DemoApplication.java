package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
public class DemoApplication implements ApplicationRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Scheduled(fixedRate = 5000)
	public void testSchedule(){
		log.info("Time =" + new Date(System.currentTimeMillis()));
	}

	@Override
	public void run(ApplicationArguments args) throws Exception{
		log.info("All Users{}",userRepository.findAll());

		User user = userService.createUser("zhangsan");
		log.info("All Users{}",userRepository.findAll());
	}

}
