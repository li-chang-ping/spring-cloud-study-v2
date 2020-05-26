package com.lcp.springcloud.service;

import org.springframework.stereotype.Component;

/**
 * @author lcp
 * @date 2020/5/26 20:58
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService{
    @Override
    public String paymentInfoOk(Integer id) {
        return "----- PaymentFallbackService fall back -- paymentInfoOk，u（；´д｀）ゞ";
    }

    @Override
    public String paymentInfoTimeOut(Integer id) {
        return "----- PaymentFallbackService fall back -- paymentInfoTimeOut，u≡(▔﹏▔)≡";
    }
}
