package com.evsyukoov.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringServer {

    public static void main(String[] args) {
        try {
            H2Server.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        SpringApplication.run(SpringServer.class, args);
    }
}
