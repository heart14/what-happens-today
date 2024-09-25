package com.liiwe.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.liiwe.base.bean.entity.DailyRecord;
import com.liiwe.mapper.DailyRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wfli
 * @since 2024/9/25 17:50
 */
@RestController
@RequestMapping(("/mysql"))
@Slf4j
public class MysqlTestController {

    private final DailyRecordMapper dailyRecordMapper;

    public MysqlTestController(DailyRecordMapper dailyRecordMapper) {
        this.dailyRecordMapper = dailyRecordMapper;
    }

    @GetMapping("/save")
    public Map<String,Integer> test() throws Exception {

        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setId(IdUtil.getSnowflakeNextId());
        dailyRecord.setDate(DateUtil.date());
        dailyRecord.setAmount("9.88");
        dailyRecord.setUsername("liwenfei");
        dailyRecord.setCreateTime(DateUtil.date());

        int save = dailyRecordMapper.insert(dailyRecord);
        Map<String, Integer> r = new HashMap<>();
        r.put("save", save);

        return r;
    }
}
