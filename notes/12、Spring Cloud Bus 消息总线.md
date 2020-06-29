# Spring Cloud Bus 消息总线

## 概述

### Spring Cloud Config 的加深与扩充

- 分布式自动刷新配置
- Spring Cloud Bus 配合 Spring Cloud Config 使用可以实现配置的动态刷新

### 是什么

![Bus+Config](E:\Developer\Java\IDEA\Practices\spring-cloud-study-v2\notes\12、Spring Cloud Bus 消息总线.assets\Bus+Config.jpeg)

Bus 支持两种消息代理：Rabbit MQ 和 Kafka

### 能干什么

![bus](E:\Developer\Java\IDEA\Practices\spring-cloud-study-v2\notes\12、Spring Cloud Bus 消息总线.assets\bus.jpeg)

### 为什么被称为总线

#### 什么是总线

在微服务架构的系统中，通常会使用轻量级的消息代理来构建一个共用的消息主题，并让系统中所有微服务实例都连接上来。由于该主题中产生的消息会被所有实例监听和消费，所以称它为消息总线。在总线上的各个实例，都可以方便地广播一些需要让其它连接在该主题上的实例都知道的消息。

#### 基本原理

Config Client 实例都监听 MQ 中同一个 topic （默认是 SpringCloudBus）。当一个服务刷新数据的时候，它会把这个信息放入到 topic 中，这样其它监听同一个 topic 的服务就能得到通知，然后去更新自身的配置。

## Rabbit MQ 环境配置

这里使用 Docker 快速部署一个 Rabbit MQ 实例

```bash
docker run --name rabbitmq_cloud -d -p 15672:15672 -p 5672:5672 rabbitmq:3.8.5-management
```

启动容器后，可以浏览器中访问[http://localhost:15672](http://localhost:15672/)来查看控制台信息，`RabbitMQ`默认的用户名：`guest`，密码：`guest`

## Spring Cloud Bus 动态刷新全局广播通知

### 以 3355 为模板制作 3366

#### pom.xml

```xml
<dependencies>
    <!--添加消息总线RabbitMQ支持-->
    <!--<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>-->

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

#### bootstrap.yaml

```yaml
server:
  port: 3366

spring:
  application:
    name: config-client-3355
  profiles:
    active:
  cloud:
    config:
      name: config-client
      profile: 3366-dev
      label: master
      uri: http://localhost:3344

eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
  instance:
    # 自定义服务名称信息
    instance-id: config-client-3366
    lease-renewal-interval-in-seconds: 1
    lease-expiration-duration-in-seconds: 2
    prefer-ip-address: true

# 暴露监控端口
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

#### ConfigClientApp3366

```java
@SpringBootApplication
@EnableEurekaClient
public class ConfigClientApp3366 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApp3366.class, args);
    }
}
```

#### ConfigClientController

```java
@RestController
@RefreshScope
public class ConfigClientController {
    @Value("${config.info}")
    private String serverPort;

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/configInfo")
    public String getConfigInfo() {
        System.out.println("serverPort"+ serverPort +"----configInfo::" + configInfo);
        return "serverPort"+ serverPort +"----configInfo::" + configInfo;
    }
}
```

### 设计思想

#### 方式一

利用消息总线触发一个客户端 /bus/refresh，而刷新所有客户端的配置（不推荐）

![bus 方式一](E:\Developer\Java\IDEA\Practices\spring-cloud-study-v2\notes\12、Spring Cloud Bus 消息总线.assets\bus 方式一.jpg)

#### 方式二

利用消息总线触发一个服务端 ConfigServer 的 `/bus/refresh` 端点，而刷新所有客户端的配置（推荐）

![bus 方式二](E:\Developer\Java\IDEA\Practices\spring-cloud-study-v2\notes\12、Spring Cloud Bus 消息总线.assets\bus 方式二.jpg)

#### 分析

方式二的架构更加合理，方式一不推荐的原因如下

- 打破了为微服务职责的单一性，因为微服务本身是业务模块，它本不应该承担配置刷新的职责
- 破坏了微服务各节点的对等性
- 有一定的局限性，例如：微服务在迁移时，它的网络地址会发生变化，此时如果想要做到自动刷新，需要增加许多额外的修改。

## Spring Cloud Bus 动态刷新定点广播通知

