package com.liiwe.base.config;

import com.liiwe.base.common.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * 自定义RedisCacheManager，配置后在使用Spring的cache注解时生效
 *
 * @author wfli
 * @since 2024/8/20 14:34
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager redisCacheManager(LettuceConnectionFactory factory) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMillis(Constants.DEFAULT_CACHE_TTL))
                .computePrefixWith(name -> name + ":")//redis2.x版本中，保存缓存时默认会在cacheNames后面加上双冒号，使用该配置改为单冒号
                .disableCachingNullValues()//禁止缓存null值
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(factory).cacheDefaults(configuration).build();
    }
}
