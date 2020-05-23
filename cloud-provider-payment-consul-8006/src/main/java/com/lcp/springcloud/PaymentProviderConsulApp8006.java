package com.lcp.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lcp
 * @date 2020/5/23 13:51
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentProviderConsulApp8006 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentProviderConsulApp8006.class, args);
    }
}
