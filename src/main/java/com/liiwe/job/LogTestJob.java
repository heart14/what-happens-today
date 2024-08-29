package com.liiwe.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wfli
 * @since 2024/8/29 13:43
 */
@Slf4j
@Component
public class LogTestJob {

    @Scheduled(cron = "* * * * * *")
    public void logTest(){
        System.out.println("--------");
        log.trace(" demo log TRACE");
        log.debug(" demo log DEBUG");
        log.info(" demo log INFO");
        log.warn(" demo log WARN");
        log.error(" demo log ERROR");
    }
}
