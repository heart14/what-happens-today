package com.liiwe.base.common;

/**
 * @author wfli
 * @since 2024/8/20 14:52
 */
public class Constants {

    /**
     * 默认缓存过期时间，单位毫秒
     */
    public static final Long DEFAULT_CACHE_TTL = 15 * 60 * 1000L;

    /**
     * 热点话题缓存名称前缀
     */
    public static final String TOPIC_CACHE_NAME_PREFIX = "dailyHot:topic:";
}
