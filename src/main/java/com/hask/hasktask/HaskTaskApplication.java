package com.hask.hasktask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HaskTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaskTaskApplication.class, args);
    }

}
