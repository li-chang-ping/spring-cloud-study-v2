package com.lcp.springboot.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

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
        int age = 10 / 0;
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
}