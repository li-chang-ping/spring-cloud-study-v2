package com.lcp.springboot.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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
     */
    @HystrixCommand(fallbackMethod = "paymentInfoTimeOutHandler")
    public String paymentInfoTimeOut(Integer id) {
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
        return "线程池:" + Thread.currentThread().getName() + " paymentInfoTimeOutHandler,id:" + id + "\t" +
                "u╥﹏╥...";
    }
}