package com.lcp.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lcp
 * @date 2020/5/23 21:53
 */
@Configuration
public class MySelfRule1 {

    @Bean
    public IRule myRule() {
        // 定义为随机
        return new RandomRule();
        // return new RoundRobinRule();
    }
}
