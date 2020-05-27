package com.lcp.springboot.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

/**
 * @author lcp
 * @date 2020/5/26 11:16
 */
@Service
public class PaymentService {
    /**
     * 正常访问
     */
    public String paymentInfoOk(Integer id) {
        return "线程池:" + Thread.currentThread().getName() + " paymentInfo_OK,id:" + id + "\t" + "O(∩_∩)O哈哈~";
    }

    /**
     * 超时访问
     *
     * <code>@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")</code>
     * 设置线程的超时时间为3秒，由于线程内设置为5秒，所以必定超时
     */
    @HystrixCommand(fallbackMethod = "paymentInfoTimeOutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public String paymentInfoTimeOut(Integer id) {
        // 计算异常
        // int age = 10 / 0;
        // 由3秒改为5秒
        int timeNumber = 5;
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池:" + Thread.currentThread().getName() + " paymentInfo_TimeOut,id:" + id + "\t" +
                "O(∩_∩)O哈哈~  耗时(秒)" + timeNumber;
    }

    public String paymentInfoTimeOutHandler(Integer id) {
        return "线程池:" + Thread.currentThread().getName() + " 系统繁忙，请稍后再试,id:" + id + "\t" +
                "u╥﹏╥...";
    }

    // ==== 服务熔断
    @HystrixCommand(fallbackMethod = "paymentCircuitBreakerFallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),// 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),// 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), // 时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),// 失败率达到多少后跳闸
    })
    public String paymentCircuitBreaker(Integer id) {
        if (id < 0) {
            throw new RuntimeException("******id 不能负数");
        }
        // UUID.randomUUID().toString();
        String serialNumber = IdUtil.simpleUUID();

        return Thread.currentThread().getName() + "\t" + "调用成功，流水号: " + serialNumber;
    }

    public String paymentCircuitBreakerFallback(Integer id) {

        return "id 不能负数，请稍后再试，/(ㄒoㄒ)/~~   id: " + id;
    }

    // @HystrixCommand(fallbackMethod = "strFallbackMethod",
    //         groupKey = "strGroupCommand",
    //         commandKey = "strCommand",
    //         threadPoolKey = "strThreadPool",
    //         commandProperties = {
    //                 // 是否启用断路器
    //                 @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
    //                 // 设置在滚动时间窗中。断路器熔断的最小请求数。例如，默认该值为 20 的时候，
    //                 // 如果滚动时间窗（默认10秒）内收到了 19 个请求，即使这 19 个请求都失败了，断路器也不会打开。
    //                 @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "20"),
    //                 // 设置断路器打开之后的休眠时间窗，休眠时间窗结束之后，会将断路器设置为半开状态，释放一次请求到原来的主逻辑上，
    //                 // 如果依然失败，断路器继续进入打开状态，休眠时间窗重新计时，如果成功则关闭。
    //                 @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
    //                 // 设置在滚动时间窗内，请求数超过 circuitBreaker.requestVolumeThreshold 的情况下，错误请求的百分比阈值（默认50），
    //                 // 超过就打开断路器，否则保持关闭
    //                 @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
    //                 // 断路器强制打开
    //                 @HystrixProperty(name = "circuitBreaker.forceOpen", value = "false"),
    //                 // 断路器强制关闭
    //                 @HystrixProperty(name = "circuitBreaker.forceClosed", value = "false"),
    //                 // 要执行的隔离策略，THREAD 线程隔离，SEMAPHORE 信号量隔离
    //                 @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
    //                 // 执行隔离线程的超时时间（单位毫秒）
    //                 @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10"),
    //                 // 是否启用超时时间
    //                 @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
    //                 // 执行隔离线程超时是否中断
    //                 @HystrixProperty(name = "execution.isolation.thread.interruptOnTimeout", value = "true"),
    //                 // 执行隔离线程将来取消时是否中断
    //                 @HystrixProperty(name = "execution.isolation.thread.interruptOnFutureCancel", value = "true"),
    //                 // 当隔离策略选择信号量时，设置最大并发数
    //                 @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "10"),
    //                 // 允许回调方法执行的最大并发数
    //                 @HystrixProperty(name = "fallback.isolation.semaphore.maxConcurrentRequests", value = "10"),
    //                 // 服务降级是否启用，是否执行回调函数
    //                 @HystrixProperty(name = "fallback.enabled", value = "true"),
    //                 // 滚动时间窗口设置，用于断路器判断健康度时需要收集信息的持续时间
    //                 @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"),
    //                 // 该属性用来设置滚动时间窗统计指标信息时划分“桶”的数量，断路器在收集指标信息时会根据设置的时间长度拆分成多个“桶”来累计
    //                 // 各度量值，每个“桶”记录一段时间内的采集指标。
    //                 // 所以 timeInMilliseconds 必需能被 numBuckets 整除，否则会抛出异常
    //                 @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "10"),
    //                 // 该属性用来设置对命令执行的延迟是否使用百分位数来跟踪计算，如果设置为 false，那么所有的概要统计都将返回 -1
    //                 @HystrixProperty(name = "metrics.rollingPercentile.enabled", value = ""),
    //                 // 设置百分位统计滚动窗口的持续时间，单位为毫秒
    //                 @HystrixProperty(name = "metrics.rollingPercentile.timeInMilliseconds", value = "60000"),
    //                 // 百分位统计滚动窗口中使用“桶“的数量
    //                 @HystrixProperty(name = "metrics.rollingPercentile.numBuckets", value = "60000"),
    //                 // 该属性用来设置在执行过程中每个”桶“中保留的最大执行次数，如果在滚动时间窗内发生超过该设定值的执行次数
    //                 // 就从最初位置开始重写，例如将该值设为 100，滚动窗口为10秒，若在10秒内一个”桶“发生了500次执行
    //                 // 那么该”桶“中只保留最后的100次执行的统计，另外，增加该值的大小将会增加内存的消耗量，并增加排序百分位数所需的计算时间。
    //                 @HystrixProperty(name = "metrics.rollingPercentile.bucketSize", value = "100"),
    //                 // 设置采集影响断路器状态的健康快照（请求的成功，错误的百分比）的间隔等待时间
    //                 @HystrixProperty(name = "metrics.healthSnapshot.intervalInMilliseconds", value = "500"),
    //                 // 是否开启请求缓存
    //                 @HystrixProperty(name = "requestCache.enabled", value = "true"),
    //                 // HystrixCommand 的执行和事件是否打印日志到 HystrixRequestLog 中
    //                 @HystrixProperty(name = "requestLog.enabled", value = "true"),
    //         },
    //         threadPoolProperties = {
    //                 // 该参数用来设置执行命令线程池的核心线程数，该值也就是命令执行的最大并发量
    //                 @HystrixProperty(name = "coreSize", value = "10"),
    //                 // 设置线程池的最大队列大小，当设置为 -1 时，线程池将使用 SynchronousQueue 实现的队列
    //                 // 否则将使用 LinkedBlockingQueue 实现的队列
    //                 @HystrixProperty(name = "maxQueueSize", value = "-1"),
    //                 // 设置拒绝阈值，通过该参数，即使队列没有达到最大值也拒绝请求
    //                 // 该参数主要是对 LinkedBlockingQueue 队列的补充，因为 LinkedBlockingQueue 队列不能
    //                 // 动态修改它的对象大小，而通过，该属性就可以调整拒绝请求的队列大小了
    //                 @HystrixProperty(name = "queueSizeRejectionThreshold", value = "5")
    //         }
    // )
    // public String strConsumer() {
    //     return "hello";
    // }
}