package com.tensquare.friend;

import com.tensquare.friend.intercept.JwtIntercept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 配置类 添加Jwt拦截器
 */
@Configuration
public class ApplicationConfig extends WebMvcConfigurationSupport {

    @Autowired
    private JwtIntercept jwtIntercept;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtIntercept).  ///添加拦截器
                addPathPatterns("/**").            //拦截所有的请求
                excludePathPatterns("/**/login"); //对login进行放行
    }
}