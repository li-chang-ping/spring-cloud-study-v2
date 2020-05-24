package com.lcp.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lcp
 * @date 2020/5/24 21:02
 */
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class ConsumerOrderFeignApp88 {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerOrderFeignApp88.class, args);
    }
}
