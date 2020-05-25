package com.lcp.springcloud.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lcp
 * @date 2020/5/25 21:28
 */
@Configuration
public class FeignConfig {

    /**
     * feignClient配置日志级别
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        // 请求和响应的头信息,请求和响应的正文及元数据
        return Logger.Level.FULL;
    }
}