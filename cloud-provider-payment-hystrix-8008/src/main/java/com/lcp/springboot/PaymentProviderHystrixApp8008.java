package com.lcp.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author lcp
 * @date 2020/5/26 11:12
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
public class PaymentProviderHystrixApp8008 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentProviderHystrixApp8008.class, args);
    }
}
