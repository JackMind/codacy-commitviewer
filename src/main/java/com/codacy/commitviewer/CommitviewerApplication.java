package com.codacy.commitviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CommitviewerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommitviewerApplication.class, args);
    }

}
