package com.lcp.springcloud.service;

import com.lcp.springcloud.entities.Payment;

/**
 * @author lcp
 * @date 2020/5/20 17:15
 */
public interface PaymentService {
    int create(Payment payment);

    Payment getPaymentById(long id);
}
