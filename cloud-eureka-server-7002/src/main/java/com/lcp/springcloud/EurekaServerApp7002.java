package com.lcp.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author lcp
 * @date 2020/5/21 20:54
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApp7002 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApp7002.class, args);
    }
}
