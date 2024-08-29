package com.liiwe.base.sys;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wfli
 * @since 2024/8/29 15:00
 */
@RestController
public class DynamicLogConfigController {

    /**
     * 可以通过接口方式，也可通过其它方式，比如数据库持久化
     */
    @PutMapping("/log/level/{level}")
    public String level(@PathVariable("level") String level) {
        // 获取日志记录器
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger("root");

        // 修改日志级别
        logger.setLevel(Level.toLevel(level));

        // 获取日志记录器后，同样可以修改其它配置，比如appender policy等
        return "log level update SUCCESS";
    }
}
