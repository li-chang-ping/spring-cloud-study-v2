package com.lcp.springcloud.service.impl;

import com.lcp.springcloud.dao.PaymentDao;
import com.lcp.springcloud.entities.Payment;
import com.lcp.springcloud.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lcp
 * @date 2020/5/20 17:16
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    @Resource
    private PaymentDao paymentDao;

    @Override
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    @Override
    public Payment getPaymentById(long id) {
        return paymentDao.getPaymentById(id);
    }
}
