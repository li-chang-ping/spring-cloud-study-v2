package com.lcp.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @author lcp
 * @date 2020/5/27 16:28
 */
@SpringBootApplication
@EnableHystrixDashboard
public class HystrixDashBoardApp9001 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashBoardApp9001.class, args);
    }
}
