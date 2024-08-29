# what-happens-today

what-happens-today?



## 1.使用redis

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



## 2.使用spring-cache缓存注解

### 介绍

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

### 1.添加依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```



### 2.开始缓存注解

```java
@SpringBootApplication
@EnableCaching //添加开启缓存注解
public class WhatHappensTodayApplication {
   public static void main(String[] args) {
      SpringApplication.run(WhatHappensTodayApplication.class, args);
   }
}
```

### 3.使用注解

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



### 4.通过第3步中进行缓存的key存在乱码

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



## 3.使用@Scheduled实现定时任务

### 1.添加依赖

已包含在spring-boot-starter中，而spring-boot-starter又包含在spring-boot-starter-web中了，所以无需另外单独引入



### 2.添加开启Schedule注解

在你的 Spring Boot 应用的主类（通常是带有 @SpringBootApplication 注解的类）中，添加 @EnableScheduling 注解以启用定时任务功能。



### 3.创建任务类

创建一个类，添加@Component注解标记为一个组件，编写具体业务方法，并在方法上添加@Scheduled注解来实现定时任务执行。注解参数可以设定定时任务执行的计划，支持固定间隔或者cron表达式。

```java
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    // 每隔 5 秒执行一次
    @Scheduled(fixedRate = 5000)
    public void performTask() {
        System.out.println("定时任务执行时间: " + System.currentTimeMillis());
    }

    // 每天的凌晨 1 点执行
    @Scheduled(cron = "0 0 1 * * ?")
    public void performDailyTask() {
        System.out.println("每天凌晨 1 点执行的任务");
    }
}
```



### 4.如果想要动态控制任务启停

在 Spring Boot 中动态控制定时任务的启停可以通过几种方法来实现，包括使用 `ScheduledFuture`、`TaskScheduler`，以及更复杂的配置方式。以下是几种常见的方式：

#### 1. 使用 `ScheduledFuture`

`ScheduledFuture` 允许你在运行时控制定时任务的启停。下面是如何使用 `ScheduledFuture` 来实现这一功能的示例：

##### 1.1 创建调度任务

```java
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ScheduledFuture;

@Component
@EnableScheduling
public class DynamicScheduledTasks {

    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    private ScheduledFuture<?> scheduledFuture;

    @PostConstruct
    public void init() {
        taskScheduler.initialize();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void performTask() {
        System.out.println("执行定时任务");
    }

    public void startTask() {
        if (scheduledFuture == null || scheduledFuture.isCancelled()) {
            scheduledFuture = taskScheduler.schedule(this::performTask, new CronTrigger("0 0 1 * * ?"));
        }
    }

    public void stopTask() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    @PreDestroy
    public void destroy() {
        taskScheduler.shutdown();
    }
}
```

##### 1.2 控制任务

你可以通过调用 `startTask()` 和 `stopTask()` 方法来启动和停止任务。例如，你可以通过 REST 控制器来实现：

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final DynamicScheduledTasks scheduledTasks;

    public TaskController(DynamicScheduledTasks scheduledTasks) {
        this.scheduledTasks = scheduledTasks;
    }

    @GetMapping("/start")
    public String start() {
        scheduledTasks.startTask();
        return "Task started";
    }

    @GetMapping("/stop")
    public String stop() {
        scheduledTasks.stopTask();
        return "Task stopped";
    }
}
```

#### 2. 使用 `TaskScheduler`

`TaskScheduler` 提供了更细粒度的控制能力，可以动态地调度任务，并且在运行时启动和停止任务。

##### 2.1 配置 `TaskScheduler`

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("Scheduled-");
        return scheduler;
    }
}
```

##### 2.2 创建任务调度组件

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ScheduledFuture;

@Component
public class TaskService {

    @Autowired
    private TaskScheduler taskScheduler;

    private ScheduledFuture<?> scheduledFuture;

    public void startTask() {
        if (scheduledFuture == null || scheduledFuture.isCancelled()) {
            scheduledFuture = taskScheduler.schedule(this::performTask, new CronTrigger("0 0 1 * * ?"));
        }
    }

    public void stopTask() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    private void performTask() {
        System.out.println("执行定时任务");
    }
}
```

#### 3. 使用 `ScheduledExecutorService`

如果不依赖 Spring 的调度机制，你也可以直接使用 `ScheduledExecutorService` 来实现定时任务的动态控制。

##### 3.1 配置和使用 `ScheduledExecutorService`

```java
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class ExecutorServiceTask {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;

    public void startTask() {
        if (scheduledFuture == null || scheduledFuture.isCancelled()) {
            scheduledFuture = scheduler.scheduleAtFixedRate(this::performTask, 0, 5, TimeUnit.SECONDS);
        }
    }

    public void stopTask() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    private void performTask() {
        System.out.println("执行定时任务");
    }
}
```

#### 总结

- **使用 `ScheduledFuture`**：适用于使用 Spring 的 `TaskScheduler`，提供了调度任务的直接控制。
- **使用 `TaskScheduler`**：可以通过配置实现任务的动态调度和控制。
- **使用 `ScheduledExecutorService`**：不依赖于 Spring 的调度机制，适用于更底层的控制需求。



