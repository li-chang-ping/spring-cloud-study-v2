package com.lcp.springcloud.service;

import com.lcp.springcloud.entities.CommonResult;

public interface IMessageProvider {
    CommonResult<Object> send();
}
