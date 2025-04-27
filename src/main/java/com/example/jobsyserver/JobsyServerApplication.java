package com.example.jobsyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobsyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobsyServerApplication.class, args);
    }

}
