package com_qust;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class quRegister {

    public static void main(String[] args){
        SpringApplication.run(quRegister.class);
    }
}
