package com.lcp.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class StreamRabbitProviderApp8801 {
    public static void main(String[] args) {
        SpringApplication.run(StreamRabbitProviderApp8801.class, args);
    }
}
