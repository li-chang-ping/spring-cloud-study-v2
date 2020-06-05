package com.lcp.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lcp
 * @date 2020/6/5 20:27
 */
@Configuration
public class GatewayConfig {
    /**
     * 配置了一个 id 为 path_route_1 的路由规则，
     * 当访问 http://192.168.1.10:9527/guonei 时会自动转发到地址 http://news.baidu.com/guonei
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        // RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        // routes.route("path_route_1",
        //         predicateSpec -> predicateSpec.path("/guonei").uri("http://news.baidu.com/guonei")).build();
        // return routes.build();

        return routeLocatorBuilder
                .routes()
                .route("path_route_1",
                        predicateSpec -> predicateSpec.path("/guonei")
                                .uri("http://news.baidu.com/guonei"))
                .build();
    }
}
