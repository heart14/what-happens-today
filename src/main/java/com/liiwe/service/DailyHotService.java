package com.liiwe.service;

import com.liiwe.base.bean.model.DailyHot;

/**
 * @author wfli
 * @since 2024/8/20 15:35
 */
public interface DailyHotService {

    DailyHot dailyHot(String name) throws Exception;
}
