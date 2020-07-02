package com.lcp.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lcp
 * @date 2020/7/1 22:25
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosPaymentProviderApp9001 {
    public static void main(String[] args) {
        SpringApplication.run(NacosPaymentProviderApp9001.class, args);
    }
}
