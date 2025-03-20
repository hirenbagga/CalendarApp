package com.hask.hasktask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class HaskTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaskTaskApplication.class, args);
    }

}
