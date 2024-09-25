package com.liiwe.controller;

import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/redis")
@Slf4j
public class RedisTestController {

    private final StringRedisTemplate redisTemplate;

    public RedisTestController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/set/{value}")
    public String redis(@PathVariable("value") String value){
        redisTemplate.opsForValue().setIfAbsent("test:key:"+value,value, 1000L,TimeUnit.SECONDS);
        return "success:"+value;
    }

    @Cacheable(cacheNames = "cacheDemo", key = "'test:cache:'+#value")
    @GetMapping("/cache/{value}")
    public String redisCache(@PathVariable("value")String value){
        log.info("caching {}",value);
        return "success:"+value;
    }
}
