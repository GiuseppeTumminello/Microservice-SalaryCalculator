package com.acoustic.enpoints;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "endpoints")
@PropertySource("classpath:microservices-endpoints.properties")
public class MicroservicesEndpoints {
}
