package com.liiwe.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liiwe.base.bean.model.DailyHot;
import com.liiwe.base.bean.model.HttpResult;
import com.liiwe.base.utils.HttpUtils;
import com.liiwe.service.DailyHotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wfli
 * @since 2024/8/20 15:35
 */
@Slf4j
@Service
public class DailyHotServiceImpl implements DailyHotService {

    @Override
    public DailyHot dailyHot(String name) throws Exception {
        HttpResult httpResult = HttpUtils.get("http://110.40.192.87:9001/" + name + "?cache=false&limit=50", null);
        log.info("{} httpResult: {}", name, httpResult);
        return JSONObject.parseObject(httpResult.getHttpResult(), DailyHot.class);
    }
}
