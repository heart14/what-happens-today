# what-happens-today

what-happens-today?



## 1.引入redis

### 1.添加依赖

```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
```

### 2.添加配置

在application.properties中添加redis相关配置：

```
#redis配置
spring.data.redis.database=0
spring.data.redis.host=
spring.data.redis.port=6379
spring.data.redis.password=
#连接超时时间
spring.data.redis.timeout=1000
#连接池最大连接数（负数表示没有限制）
spring.data.redis.lettuce.pool.max-active=8
#连接池最大阻塞等待时间（负数表示没有限制）
spring.data.redis.lettuce.pool.max-wait=-1
#连接池最大空闲连接数
spring.data.redis.lettuce.pool.max-idle=8
#连接池最小空闲连接数
spring.data.redis.lettuce.pool.min-idle=-1
```

3.使用Redis

在代码中注入RedisTemplate依赖，即可进行redis的操作了：

```java
    private final StringRedisTemplate redisTemplate;

public DemoController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        }

@GetMapping("/{value}")
public String redis(@PathVariable("value") String value){
        redisTemplate.opsForValue().setIfAbsent("test:key:"+value,value, 1000L,TimeUnit.SECONDS);
        return "success:"+value;
        }
```

但要注意：如果直接引入RedisTemplate redisTemplate，存入数据会乱码，所以引入StringRedisTemplate



## 2.使用缓存注解

## 介绍

Spring 的缓存注解用于简化缓存操作，以下是几个常用的注解及其说明：

**@Cacheable**: 用于标记方法结果可以被缓存。方法被调用时，Spring 会先检查缓存中是否已有结果，如果有则直接返回缓存的结果，否则调用方法并将结果存入缓存中。

```java
@Cacheable(value = "cacheName", key = "#param")
public String getData(String param) {
    // 方法实现
}
```

**@CachePut**: 用于更新缓存。每次调用被注解的方法时，都会更新缓存中的值，而不会影响方法的执行。

```java
@CachePut(value = "cacheName", key = "#param")
public String updateData(String param) {
    // 方法实现
}
```

**@CacheEvict**: 用于从缓存中移除数据。可以用于删除单个或多个缓存条目。`allEntries` 属性可以设置为 `true`，以清空整个缓存。

```java
@CacheEvict(value = "cacheName", key = "#param")
public void removeData(String param) {
    // 方法实现
}

@CacheEvict(value = "cacheName", allEntries = true)
public void clearCache() {
    // 清空整个缓存
}
```

**@Caching**: 用于组合使用多个缓存注解，允许在一个方法上应用多个缓存操作，如 `@Cacheable` 和 `@CacheEvict`。

```java
@Caching(
    cacheable = @Cacheable(value = "cacheName", key = "#param"),
    evict = @CacheEvict(value = "cacheName", key = "#param")
)
public String processData(String param) {
    // 方法实现
}
```

## 1.添加依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```



## 2.开始缓存注解

```java
@SpringBootApplication
@EnableCaching //添加开启缓存注解
public class WhatHappensTodayApplication {
   public static void main(String[] args) {
      SpringApplication.run(WhatHappensTodayApplication.class, args);
   }
}
```

## 3.使用注解

```java
@Cacheable(cacheNames = "cacheDemo", key = "'test:cache:'+#value")
@GetMapping("/cache/{value}")
public String redisCache(@PathVariable("value")String value){
    System.out.println("caching "+value);
    return "success:"+value;
}
```

说明：

注意区分@Cacheable注解中cacheNames和key字段的区别



4.通过第3步中进行缓存的key存在乱码

自定义RedisCacheManager来解决：

```java
@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager redisCacheManager(LettuceConnectionFactory factory) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMillis(30 * 1000L))
                .computePrefixWith(name -> name + ":")//redis2.x版本中，保存缓存时默认会在cacheNames后面加上双冒号，使用该配置改为单冒号
                .disableCachingNullValues()//禁止缓存null值
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(factory).cacheDefaults(configuration).build();
    }
}
```

同时配置了默认缓存过期事件，修改了缓存名中的双冒号问题