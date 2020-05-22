package com.lcp.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author lcp
 * @date 2020/5/20 15:20
 */
@SpringBootApplication
@EnableEurekaClient
public class PaymentProviderApp8002 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentProviderApp8002.class, args);
    }
}
