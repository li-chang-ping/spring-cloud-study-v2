package com.lcp.springcloud.controller;

import com.lcp.springcloud.entities.CommonResult;
import com.lcp.springcloud.service.IMessageProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lcp
 * @date 2020/6/30 16:55
 */
@RestController
public class SendMessageController {
    @Resource(name = "messageProviderImpl")
    private IMessageProvider iMessageProvider;

    @GetMapping(value = "/sendMessage")
    private CommonResult<Object> sendMessage() {
        return iMessageProvider.send();
    }
}
