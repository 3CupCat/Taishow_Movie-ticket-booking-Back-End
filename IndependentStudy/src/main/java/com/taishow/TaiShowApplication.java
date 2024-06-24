package com.taishow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaiShowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaiShowApplication.class, args);
    }
}