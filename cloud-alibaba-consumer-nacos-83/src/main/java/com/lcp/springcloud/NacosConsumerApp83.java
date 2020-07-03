package com.lcp.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lcp
 * @date 2020/7/3 10:50
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosConsumerApp83 {
    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerApp83.class, args);
    }
}
