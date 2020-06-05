package com.lcp.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author lcp
 * @date 2020/6/5 14:26
 */
@SpringBootApplication
@EnableEurekaClient
public class GatewayApp9527 {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApp9527.class,args);
    }
}
