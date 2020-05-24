package com.lcp.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author lcp
 * @date 2020/5/21 10:24
 */
@SpringBootApplication
@EnableEurekaClient
public class ConsumerOrderApp81 {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerOrderApp81.class, args);
    }
}
