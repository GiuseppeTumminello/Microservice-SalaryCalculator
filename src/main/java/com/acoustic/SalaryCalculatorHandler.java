package com.acoustic;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SalaryCalculatorHandler {
    public static void main(String[] args) {
        SpringApplication.run(SalaryCalculatorHandler.class, args);
    }
}
