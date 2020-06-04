# Gateway 新一代路由网关

## 概述简介

### 官网

#### 上一代 Zuul 1.x

https://github.com/Netflix/zuul/wiki

#### 当前 Gateway

https://cloud.spring.io/spring-cloud-gateway/2.2.x/reference/html/

### 是什么

#### 概述

Gateway 是在 Spring 生态系统上构建的 API 网关服务，基于 Spring 5、Spring Boot 2 和 Project Reactor 等技术。

Gateway 旨在提供一种简单有效的方式来对 API 进行路由，以及提供一些强大的过滤器功能，例如：熔断、限流、重试等。

SpringCloud Gateway 是 SpringCloud 的一个全新项目，基于 Spring 5.0 + Spring Boot 2.0 + 和 Project Reactor 等技术开发的网关，它旨在为微服务架构简单有效的统一的 API 路由管理方式。

SpringCloud Gateway 作为 Spring Cloud 生态系统中的网关、目标是替代 Zuul、在 Spring Cloud 2.0 以上版本中，没有对新版本的 Zuul 2.0 以上最新高性能版本进行集成，仍然还是使用的 Zuul 1.x 非 Reactor 模式的老版本。而为了提升网关性能，SpringCloud Gateway 是基于 WebFlux 框架实现的，而 WebFlux 框架底层则使用了高性能的 Reactor 模式通信框架 Netty。

SpringCloud Gateway 提供了统一的路由方式且基于 Filter 链的方式提供了网关基本的功能，例如：安全，监控/指标，和限流。

#### 一句话

SpringCloud Gateway 使用的 WebFlux 中的 Reactor-Netty 响应式编程组件，底层使用了 Netty 通讯框架

### 能干嘛

1. 反向代理
2. 鉴权
3. 流量监控
4. 熔断
5. 日志监控
6. ........

### 微服务架构中网关的位置

![微服务中网关的位置](10、Gateway 新一代路由网关.assets/微服务中网关的位置.png)

### 为什么会有 Gateway

#### 为什么选择 Gateway

##### Netflix 的 Zuul2.0 一直跳票，迟迟不发布

- 一方面因为 Zuul 1.0 已经进入了维护阶段，而且 Gateway 是 SpringCloud 团队研发的，是亲儿子产品，用起来简单便捷，值得信赖。而且很多功能 Zuul 都没有。

- Gateway 是基于 `异步非阻塞模型` 开发的，性能方面无需担心。虽然 Netflix 早就发布了最新的 Zuul 2.x，

  但 Spring Cloud 目前没有整合计划，而且 Netflix 相关部件都宣布进入维护期，不知前景如何。

- 多方面综合考虑 Gateway 是很理想的网关选择。

##### SpringCloud Gateway 具有如下特性

- 基于 Spring Framework 5，Project Reactor 和 Spring Boot 2.0 构建
- 动态路由：能够匹配任何请求属性
- 可以对路由指定 Predicate（断言）和 Filter（过滤器）
- 集成 Hystrix 的断路器功能
- 集成 Spring Cloud 服务发现功能
- 易于编写的 Predicate（断言）和 Filter（过滤器）
- 请求限流
- 支持路径重写

##### SpringCloud Gateway 与 Zuul 的区别

在 SpringCloud Finchley 正式版之前，SpringCloud 推荐的网关是 Netflix 提供的 Zuul。

- Zuul 1.x 是一个基于阻塞 I/O 的 API Gateway
- Zuul 1.x 基于 Servlet 2.5 使用阻塞架构它不支持任何长连接（如 WebSocket）。Zuul 的设计模式和 Nginx 较像，每次 I/O 操作都是从工作线程中选择一个执行，请求线程被阻塞到工作线程完成，但是差别是 Nginx 用 C++ 实现，Zuul 用 Java 实现，而 JVM 本身会有第一次加载较慢的情况，使得 Zuul 的性能相对较差。
- Zuul 2.x 的理念更为先进，想基于 Netty 非阻塞和支持长连接，但 SpringCloud 目前还没有整合。
- Spring Cloud Gateway 使用非阻塞 API，支持 WebSocket，与 Spring 紧密集成开发体验更好。

#### Zuul 1.x 模型

SpringCloud 中所集成的 Zuul 版本，采用的是 Tomcat 容器，使用的是传统的 Servlet IO 处理模型。

Servlet 由 Servlet Container 进行生命周期管理，Container 启动时构造 Servlet 对象并调用 servlet init() 进行初始化，Container 运行时接受请求，并为每个请求分配一个线程（一般从线程池中获取空闲线程）然后调用 service()，Container 关闭时调用 servlet destroy() 销毁 servlet；

![image-20200604195730719](10、Gateway 新一代路由网关.assets/image-20200604195730719.png)

上述模式的缺点

Servlet 是一个简单的网络 IO 模型，当请求进入 Servlet Container 时，Servlet Container 就会为其绑定一个线程，在`并发不高的场景下`这种模型是适用的。但是一旦高并发，线程数量就会大幅上涨，而线程资源代价是昂贵的（上下文切换，内存消耗大）严重影响请求的处理时间。在一些简单的业务场景下，不希望为每个 request 分配一个线程，只需要 1 个或几个线程就能应对极大的并发请求，这种业务场景下 Servlet 没有优势。

因此 Zuul 1.x 是基于 Servlet 之上的一个阻塞式处理模型，即 Spring 实现了处理所有 request 请求的一个 Servlet（DispatcherServlet）并由该 Servlet 阻塞式处理，Zuul 1.x 无法摆脱 Servlet 模型的弊端。

#### Gateway 是什么

##### WebFlux

官网：https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#spring-webflux

传统的 Web 框架，比如说：struts 2，springmvc 等都是基于 Servlet API 与 Servlet 容器基础之上运行的。

但是在 Servlet 3.1 之后有了异步非阻塞支持。而 WebFlux 是一个典型的非阻塞异步的框架，它的核心是基于 Reactor 的相关 API 实现的。相对于传统 Web 框架来说，它可以运行在诸如 Netty，Undertow 及支持 Servlet3.1 的容器上。非阻塞式 + 函数式编程（Spring 5必须让你使用 Java8）

Spring WebFlux 是 Spring 5.0 引入的新的响应式框架，区别于 SpingMVC，它不需要依赖 Servlet API，它完全是异步非阻塞的，并且基于 Reactor 来实现响应式流规范。

## 三大核心概念

### Route（路由）

路由是构建网关的基本模块，它由 ID，目标 URI，一系列的断言和过滤器组成，如断言为 true 则匹配该路由

### Predicate（断言）

参考的是 Java8 的 java.util.function.Predicate

开发人员可以匹配 HTTP 请求中的所有内容（例如请求头或请求参数），如果请求与断言相匹配则进行路由。

### Filter（过滤）

总结

指的是 Spring 框架中 Gateway Filter 的实例，使用过滤器，可以在请求被路由前或者路由后对请求进行修改。

![image-20200604210753794](10、Gateway 新一代路由网关.assets/image-20200604210753794.png)

web 请求，通过一些匹配条件，定位到真正的服务节点，并在这个转发过程的前后，进行一些精细化控制。

Predicate 就是我们的匹配条件；而 Filter，就可以理解为一个无所不能的拦截器，有了这两个元素，再加上目标 url，就可以实现一个具体的路由了。

## Gateway工作流程

### 官网介绍

![image-20200604213038208](10、Gateway 新一代路由网关.assets/image-20200604213038208.png)

客户端向 Spring Cloud Gateway 发出请求，然后在 Gateway Handler Mapping 中找到与请求相匹配的路由，将其发送到 Web Handler。

Handler 再通过指定的过滤器来将请求发送到我们实际的服务执行业务逻辑，然后返回。过滤器之间用虚线分开是因为过滤器可能会在发送代理请求之前（"pre"）或之后（"post"）执行业务逻辑。

Filter 在 "pre" 类型的过滤器可以做参数校验、权限校验、流量监控、日志输出、协议转换等。在 "post" 类型的过滤器中可以做响应内容、响应头的修改、日志的输出、流量监控等有着非常重要的作用。

### 核心逻辑

`路由转发 + 执行过滤器链`

## 入门配置



## 通过微服务名实现动态路由



## Predicate的使用



## Filter的使用



