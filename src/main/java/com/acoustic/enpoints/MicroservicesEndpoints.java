package com.acoustic.enpoints;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "microservice")
@PropertySource("classpath:microservices-endpoints.properties")
@Getter
public class MicroservicesEndpoints {
    @Value("${microservice.endpoint}")
    private List<String> endpoints;
}
