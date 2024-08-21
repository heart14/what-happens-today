package com.liiwe.job;

import com.liiwe.base.bean.entity.Topic;
import com.liiwe.base.bean.model.DailyHot;
import com.liiwe.base.common.Constants;
import com.liiwe.base.enums.PlatformEnums;
import com.liiwe.service.DailyHotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wfli
 * @since 2024/8/20 16:19
 */
@Slf4j
@Component
public class FetchHotTopicJob {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final DailyHotService dailyHotService;
    private final StringRedisTemplate redisTemplate;

    public FetchHotTopicJob(DailyHotService dailyHotService, StringRedisTemplate redisTemplate) {
        this.dailyHotService = dailyHotService;
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(cron = "0 * * * * *")
    public void fetchHotTopic() {
        // 获取当前时间并格式化
        String key = Constants.TOPIC_CACHE_NAME_PREFIX + dateTimeFormatter.format(LocalDateTime.now());
        log.info("fetchHotTopic job exec {}", key);
        // 获取平台名称列表
        List<String> names = PlatformEnums.nameList();
        // 使用流处理并合并所有主题标题
        List<String> topics = names.stream()
                .flatMap(name -> {
                    try {
                        DailyHot dailyHot = dailyHotService.dailyHot(name);
                        return dailyHot.getData().stream().map(Topic::getTitle);
                    } catch (Exception e) {
                        // 记录异常
                        log.error("fetchHotTopic for platform {} ", name, e);
                        // 发生异常时返回空流
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
        // 将主题标题列表推送到 Redis
        redisTemplate.opsForList().leftPushAll(key, topics);
        // 设置过期时间
        redisTemplate.expire(key, Constants.DEFAULT_CACHE_TTL, TimeUnit.MILLISECONDS);
    }
}
