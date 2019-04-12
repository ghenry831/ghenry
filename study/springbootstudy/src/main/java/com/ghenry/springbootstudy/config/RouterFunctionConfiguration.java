package com.ghenry.springbootstudy.config;

import com.ghenry.springbootstudy.pojo.User;
import com.ghenry.springbootstudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Configuration
public class RouterFunctionConfiguration {


    @Bean
    @Autowired
    public RouterFunction<ServerResponse> personFindAll(UserRepository userRepository){


        return RouterFunctions.route(RequestPredicates.GET("/person/find/all"),request ->{

            Collection<User> users = userRepository.findAll();
            Flux<User> userFlux = Flux.fromIterable(users);
            return ServerResponse.ok().body(userFlux,User.class);
        });
    }
}
