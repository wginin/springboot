package com.tensquare.qa.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 客户端调用服务端接口 编码 需要跟服务端保持一致
 */
@FeignClient("tensquare-base") //配置客户端调用服务单的服务名称
@RequestMapping("/label")
public interface LabelClient {

    /**
     * findById
     */
    @RequestMapping(value = "/{labelId}",method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String labelId);
}
