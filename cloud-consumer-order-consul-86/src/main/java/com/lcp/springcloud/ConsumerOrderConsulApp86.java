package com.lcp.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lcp
 * @date 2020/5/23 14:29
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConsumerOrderConsulApp86 {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerOrderConsulApp86.class, args);
    }
}
