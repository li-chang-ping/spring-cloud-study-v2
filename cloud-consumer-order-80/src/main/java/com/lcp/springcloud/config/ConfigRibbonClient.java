package com.lcp.springcloud.config;

import com.lcp.myrule.MySelfRule1;
import com.lcp.myrule.MySelfRule2;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author lcp
 * @date 2020/5/24 17:10
 */
@Configuration
// @RibbonClient(name = "CLOUD-PROVIDER-PAYMENT", configuration = MySelfRule.class)
@RibbonClients(value = {@RibbonClient(name = "CLOUD-PROVIDER-PAYMENT", configuration = MySelfRule1.class)},
        defaultConfiguration = MySelfRule2.class)
public class ConfigRibbonClient {
}