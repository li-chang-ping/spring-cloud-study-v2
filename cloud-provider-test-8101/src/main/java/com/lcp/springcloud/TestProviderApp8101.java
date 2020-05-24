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
public class TestProviderApp8101 {
    public static void main(String[] args) {
        SpringApplication.run(TestProviderApp8101.class, args);
    }
}
