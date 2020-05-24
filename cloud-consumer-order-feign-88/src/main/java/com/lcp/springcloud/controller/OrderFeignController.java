package com.lcp.springcloud.controller;

import com.lcp.springcloud.entities.CommonResult;
import com.lcp.springcloud.entities.Payment;
import com.lcp.springcloud.service.PaymentFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lcp
 * @date 2020/5/24 21:12
 */
@RestController
@Slf4j
@RequestMapping(value = "/consumer/payment")
public class OrderFeignController {
    @Resource
    private PaymentFeignService paymentFeignService;

    @GetMapping(value = "/get/{id}")
    CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        CommonResult<Payment> paymentById = paymentFeignService.getPaymentById(id);
        log.info(paymentById.toString());
        return paymentById;
    }
}
