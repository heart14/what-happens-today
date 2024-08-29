package com.liiwe.base.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wfli
 * @since 2024/8/29 15:20
 */
@Component
public class InitLogConfig {

    @PostConstruct
    public void initLogConfig(){
        // 比如在spring boot应用启动时，读取数据库，获取当前持久化的log配置
        // 假如当前保存在数据库里的日志级别是ALL
        String level = "INFO";

        // 获取日志记录器
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger("root");
        logger.debug("init log level config :{}", level);

        // 修改日志级别
        logger.setLevel(Level.toLevel(level));
    }
}
