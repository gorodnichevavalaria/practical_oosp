package com.example.resultapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ResultapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResultapiApplication.class, args);
    }
}