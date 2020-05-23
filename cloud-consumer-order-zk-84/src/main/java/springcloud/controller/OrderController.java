package springcloud.controller;

import com.lcp.springcloud.entities.CommonResult;
import com.lcp.springcloud.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author lcp
 * @date 2020/5/21 10:44
 */
@RestController
@Slf4j
@RequestMapping(value = "/consumer/payment")
public class OrderController {
    public static final String PAYMENT_URL = "http://cloud-provider-payment";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping(value = "/zk")
    public String paymentInfo() {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/zk", String.class);
    }
}
