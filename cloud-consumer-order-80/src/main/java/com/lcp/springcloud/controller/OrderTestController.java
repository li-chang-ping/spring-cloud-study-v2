package com.lcp.springcloud.controller;

import com.lcp.springcloud.entities.CommonResult;
import com.lcp.springcloud.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author lcp
 * @date 2020/5/21 10:44
 */
@RestController
@Slf4j
@RequestMapping(value = "/consumer/test")
public class OrderTestController {
    public static final String PAYMENT_URL = "http://CLOUD-PROVIDER-TEST";

    @Resource
    private RestTemplate restTemplate;

    @PostMapping("/create")
    public CommonResult create(@RequestBody Payment payment) {
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
    }

    @GetMapping(value = "/get/{id}")
    public CommonResult getPayment(@PathVariable Long id) {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
    }

    @RequestMapping(value = "/getForEntity/{id}")
    public CommonResult getPayment2(@PathVariable String id) {
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
        log.info(entity.toString());
        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        } else {
            return new CommonResult<>(444, "ERROR");
        }
    }
}
