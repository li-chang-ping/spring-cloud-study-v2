package com.lcp.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lcp
 * @date 2020/5/22 21:33
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentProviderZkApp8004 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentProviderZkApp8004.class, args);
    }
}
