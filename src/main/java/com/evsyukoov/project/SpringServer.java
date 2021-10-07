package com.evsyukoov.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringServer {

    public static void main(String[] args) throws Exception {
        try {
            H2Server.run();
            SpringApplication.run(SpringServer.class, args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
