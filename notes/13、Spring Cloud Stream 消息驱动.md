# Spring Cloud Stream 消息驱动

官方文档：https://cloud.spring.io/spring-cloud-static/spring-cloud-stream/3.0.6.RELEASE/reference/html/

第三方中文版：

1. https://www.springcloud.cc/spring-cloud-greenwich.html#_spring_cloud_stream
2. https://m.wang1314.com/doc/webapp/topic/20971999.html

## 消息驱动概述

### 是什么

**屏蔽底层消息中间件的差异，降低切换成本，统一消息的编程模型**

### Spring Cloud Stream 是什么

官方定义 Spring Cloud Stream 是一个构建消息驱动微服务的框架

应用程序通过 inputs 或者 outputs 来与 Spring Cloud Stream 中的 binder 对象交互。

我们通过配置来绑定（binding），而 Spring Cloud Stream 的 binder 对象负责与消息中间件交互。

所以，我们只需要搞清楚如何与 Spring Cloud Stream 交互就可以方便的使用消息驱动。

Spring Cloud Stream 通过 Spring Integration 来连接消息代理中间件以实现消息时间驱动。

Spring Cloud Stream 为一些消息中间件产品提供了个性化的自动化配置实现，引用了：**发布-订阅、消费组、消费分区** 三个核心概念。

目前仅支持 RabbitMQ、Kafka

### 设计思想

#### 标准 MQ

![image-20200630110713567](E:\Developer\Java\IDEA\Practices\spring-cloud-study-v2\notes\13、Spring Cloud Stream 消息驱动.assets\image-20200630110713567.png)

生产者/消费者之间靠消息媒介传递消息内容

- Message

消息必须走特定通道

- MessageChannel

消息通道里的消息如何被消费呢，谁负责收发处理

- 消息通道 MessageChannel 的子接口 SubscribeChannel，由 MessageHandler 消息处理器订阅

#### 使用 Spring Cloud Stream 的原因

假如说用到了 RabbitMQ 和 Kafka，这两个消息中间件的架构有所上不同，像 RabbitMQ 有 Exchange，Kafaka 有 Topic 和 Partitions 分区。

这些中间件的差异性会给实际项目开发造成一定困扰，我们如果用了两个消息队列的一种，后面的业务需求如果要迁往另外一种消息队列，需要推倒重做很多东西，因为这些消息中间件与系统耦合，Spring Cloud Stream 可以帮助我们解耦。

![image-20200630112616166](E:\Developer\Java\IDEA\Practices\spring-cloud-study-v2\notes\13、Spring Cloud Stream 消息驱动.assets\image-20200630112616166.png)

#### Stream 可以统一底层差异的原因 Binder

在没有绑定器这个概念的情况下，我们的 SpringBoot 应用要直接与消息中间件进行信息交互的时候，由于各消息中间件的初衷不同，它们的实现细节上会有较大的差异性。

**通过定义绑定器（BInder）作为中间层，完美的实现了应用程序与消息中间件细节之间的隔离**。

通过向应用程序暴露统一的 Channel 通道，是得应用程序不需要再考虑各种不同的消息中间件实现。

通过 Stream 对消息中间件的进一步封装，可以做到代码层面对中间件无感知，甚至于动态的切换中间件(rabbitmq切换为Kafka)，使得微服务开发高度解耦，服务可以更多关注自己的业务流程。

#### Binder

input 对应消费者

output 对应生产者

#### Stream 中的消息通信方式遵循了 发布-订阅 模式

Topic 主题进行广播

- RabbitMQ 中就是 Exchange
- Kafka 中就是 Topic

### Spring Cloud Stream 标准流程套路

![Binder](E:\Developer\Java\IDEA\Practices\spring-cloud-study-v2\notes\13、Spring Cloud Stream 消息驱动.assets\Binder.jpeg)

![生产者消费者](E:\Developer\Java\IDEA\Practices\spring-cloud-study-v2\notes\13、Spring Cloud Stream 消息驱动.assets\生产者消费者.jpeg)

#### Binder

非常方便的连接中间件，屏蔽差异。

#### Channel

通道，是队列 Queue 的一种抽象，在消息通讯系统中就是实现存储和转发的媒介，通过 Channel 对队列进行配置

#### Source & Sink

简单的可理解为参照对象是 Spring Cloud Stream，从 Stream 发布消息就是输出，接收消息就是输入。

### 编码 API 和常用注解

![编码API和常用注解](E:\Developer\Java\IDEA\Practices\spring-cloud-study-v2\notes\13、Spring Cloud Stream 消息驱动.assets\编码API和常用注解.jpeg)

## 案例说明

前提：RabbitMQ 环境已经 OK

新建 8801 生产者模块发布消息

新建 8802/8803 消费者模块接收消息

## 消息驱动-生产者

新建 cloud-stream-rabbit-provider-8801 

### POM

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-eureka-server -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

    <dependency>
        <groupId>com.lcp.springcloud</groupId>
        <artifactId>cloud-api-commons</artifactId>
        <version>${project.version}</version>
    </dependency>


    <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>


    <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### YML

```yaml
server:
  port: 8801

spring:
  application:
    name: cloud-stream-provider
  cloud:
    stream:
      binders: # 在此处配置要绑定的rabbitmq的服务信息；
        defaultRabbit: # 表示定义的名称，用于于binding整合
          type: rabbit # 消息组件类型
          environment: # 设置rabbitmq的相关的环境配置
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest
      bindings: # 服务的整合处理
        output: # 这个名字是一个通道的名称
          destination: studyExchange # 表示要使用的Exchange名称定义
          content-type: application/json # 设置消息类型，本次为json，文本则设置“text/plain”
          binder: defaultRabbit  # 设置要绑定的消息服务的具体设置

eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
  instance:
    instance-id: provider-8801
    lease-renewal-interval-in-seconds: 1
    lease-expiration-duration-in-seconds: 2
    prefer-ip-address: true
```

### 主启动类

```java
@SpringBootApplication
@EnableEurekaClient
public class StreamRabbitProviderApp8801 {
    public static void main(String[] args) {
        SpringApplication.run(StreamRabbitProviderApp8801.class, args);
    }
}
```

### 业务类

#### 发送消息接口

```java
public interface IMessageProvider {
    CommonResult<Object> send();
}
```

#### 发送消息接口实现类

```java
@EnableBinding(Source.class)    // 定义消息推送管道
public class MessageProviderImpl implements IMessageProvider {
    @Resource
    private MessageChannel output;

    @Override
    public CommonResult<Object> send() {
        String serial = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(serial).build());
        System.out.println("serial::::" + serial);
        return new CommonResult<>(200, "发送成功", serial);
    }
}
```

#### Controller

```java
@RestController
public class SendMessageController {
    @Resource(name = "messageProviderImpl")
    private IMessageProvider iMessageProvider;

    @GetMapping(value = "/sendMessage")
    private CommonResult<Object> sendMessage() {
        return iMessageProvider.send();
    }
}
```

### 测试

启动 7001，7002，rabbitmq，8801

访问：http://localhost:8801/sendMessage

![image-20200630170831870](E:\Developer\Java\IDEA\Practices\spring-cloud-study-v2\notes\13、Spring Cloud Stream 消息驱动.assets\image-20200630170831870.png)

![image-20200630171749882](E:\Developer\Java\IDEA\Practices\spring-cloud-study-v2\notes\13、Spring Cloud Stream 消息驱动.assets\image-20200630171749882.png)

多次快速刷新：http://localhost:8801/sendMessage

![image-20200630171859461](E:\Developer\Java\IDEA\Practices\spring-cloud-study-v2\notes\13、Spring Cloud Stream 消息驱动.assets\image-20200630171859461.png)

## 消息驱动-消费者

新建 cloud-stream-rabbit-provider-8802、cloud-stream-rabbit-provider-8803

以下以 cloud-stream-rabbit-provider-8802 为例

### POM

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-eureka-server -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

    <dependency>
        <groupId>com.lcp.springcloud</groupId>
        <artifactId>cloud-api-commons</artifactId>
        <version>${project.version}</version>
    </dependency>


    <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>


    <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### YML

> 注意：bindings 下面不再是 output，而是 input

```yml
server:
  port: 8802

spring:
  application:
    name: cloud-stream-consumer
  cloud:
    stream:
      binders: # 在此处配置要绑定的rabbitmq的服务信息；
        defaultRabbit: # 表示定义的名称，用于于binding整合
          type: rabbit # 消息组件类型
          environment: # 设置rabbitmq的相关的环境配置
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest
      bindings:
        input: # 这个名字是一个通道的名称，因为是消费者，所以是 input
          destination: studyExchange # 表示要使用的Exchange名称定义
          content-type: application/json # 设置消息类型，本次为json，文本则设置“text/plain”
          binder: defaultRabbit  # 设置要绑定的消息服务的具体设置

eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
  instance:
    instance-id: provider-8802
    lease-renewal-interval-in-seconds: 1
    lease-expiration-duration-in-seconds: 2
    prefer-ip-address: true
```

### 主启动类

```java
@SpringBootApplication
public class StreamRabbitConsumerApp8802 {
    public static void main(String[] args) {
        SpringApplication.run(StreamRabbitConsumerApp8802.class, args);
    }
}
```

### 业务类

```java
@Component
@EnableBinding(Sink.class)
public class ReceiveMessageListenerController {
    @Value("${server.port}")
    private String serverPort;

    @StreamListener(Sink.INPUT)
    private void input(Message<String> message) {
        System.out.println("Module 8802，接收："+message.getPayload()+"\t port："+serverPort);
    }
}
```

### 测试

启动 7001，7002，rabbitmq，8801，8802，8803 这里不启动

查看 rabbitmq 的管理界面，会发现此时有一个队列绑定到了 studyExchange 上。

![image-20200630201241925](E:\Developer\Java\IDEA\Practices\spring-cloud-study-v2\notes\13、Spring Cloud Stream 消息驱动.assets\image-20200630201241925.png)

测试 8801 发送，8802 接收，访问：http://localhost:8801/sendMessage

![image-20200630201451356](E:\Developer\Java\IDEA\Practices\spring-cloud-study-v2\notes\13、Spring Cloud Stream 消息驱动.assets\image-20200630201451356.png)

## 简化配置

因为使用的 starter 就是 spring-cloud-starter-stream-rabbit

以 8801 为例，生产者配置可简化为如下配置，

```yaml
server:
  port: 8801

spring:
  application:
    name: cloud-stream-provider
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
  cloud:
    stream:
      bindings:
        output: # 这个名字是一个通道的名称，因为是生产者，所以是 output
          destination: studyExchange # 表示要使用的Exchange名称定义
          content-type: application/json # 设置消息类型，本次为json，文本则设置“text/plain”

eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
  instance:
    instance-id: provider-8801
    lease-renewal-interval-in-seconds: 1
    lease-expiration-duration-in-seconds: 2
    prefer-ip-address: true
```

以 8802 为例，消费者配置可以简化为如下配置

```yaml
server:
  port: 8802

spring:
  application:
    name: cloud-stream-consumer
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
  cloud:
    stream:
      bindings:
        input: # 这个名字是一个通道的名称，因为是消费者，所以是 input
          destination: studyExchange # 表示要使用的Exchange名称定义
          content-type: application/json # 设置消息类型，本次为json，文本则设置“text/plain”

eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
  instance:
    instance-id: provider-8802
    lease-renewal-interval-in-seconds: 1
    lease-expiration-duration-in-seconds: 2
    prefer-ip-address: true
```

经过测试，依然可以正常使用