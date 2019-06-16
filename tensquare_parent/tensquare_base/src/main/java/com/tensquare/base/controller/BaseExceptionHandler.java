package com.tensquare.base.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import entity.Result;
import entity.StatusCode;

/**
 * 自定义异常类
 * ControllerAdvice:控制层拦截异常
 * RestControllerAdvice == ControllerAdvice+ ResponseBody
 *
 */
@ControllerAdvice
public class BaseExceptionHandler {


    //定一个异常方法处理 返回异常结果  ExceptionHandler:处理控制层异常
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e){
        return new Result(false, StatusCode.ERROR,e.getMessage());
    }
}
