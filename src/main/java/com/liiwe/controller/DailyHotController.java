package com.liiwe.controller;

import com.liiwe.base.bean.model.DailyHot;
import com.liiwe.service.DailyHotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wfli
 * @since 2024/8/20 15:33
 */
@RestController
@RequestMapping(("/hot"))
@Slf4j
public class DailyHotController {

    private final DailyHotService dailyHotService;

    public DailyHotController(DailyHotService dailyHotService) {
        this.dailyHotService = dailyHotService;
    }

    /**
     * 当前热点话题
     * @return
     */
    @GetMapping("/current")
    public DailyHot current() throws Exception{
        return dailyHotService.dailyHot();
    }
}
