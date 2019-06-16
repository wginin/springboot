package com.tensquare.gathering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

@SpringBootApplication
@EnableCaching //开启spring cache缓存
//1.redisTemplate需要编码   spring cache 注解配置即可
//2.redisTemplate可以设置失效时间   spring cache 不可以设置
//3.根据具体业务需求来定，不需要清除缓存数据 ：spring cache 方式    需要定期定时清除缓存：RedisTemplate更为灵活
@EnableEurekaClient
public class GatheringApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatheringApplication.class, args);
    }

    @Bean
    public IdWorker idWorkker() {
        return new IdWorker(1, 1);
    }

}
