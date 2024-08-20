package com.liiwe.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author wfli
 * @since 2024/8/19 23:59
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    private final StringRedisTemplate redisTemplate;

    public DemoController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/redis/{value}")
    public String redis(@PathVariable("value") String value){
        redisTemplate.opsForValue().setIfAbsent("test:key:"+value,value, 1000L,TimeUnit.SECONDS);
        return "success:"+value;
    }

    @Cacheable(cacheNames = "cacheDemo", key = "'test:cache:'+#value")
    @GetMapping("/cache/{value}")
    public String redisCache(@PathVariable("value")String value){
        System.out.println("caching "+value);
        return "success:"+value;
    }
}
