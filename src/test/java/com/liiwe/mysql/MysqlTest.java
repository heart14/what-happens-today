package com.liiwe.mysql;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.liiwe.base.bean.entity.DailyRecord;
import com.liiwe.mapper.DailyRecordMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wfli
 * @since 2024/9/25 16:30
 */
@SpringBootTest
public class MysqlTest {

    @Autowired
    private DailyRecordMapper dailyRecordMapper;

    @Test
    public void mysqlTest() {

        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setId(IdUtil.getSnowflakeNextId());
        dailyRecord.setDate(DateUtil.date());
        dailyRecord.setAmount("9.88");
        dailyRecord.setUsername("liwenfei");
        dailyRecord.setCreateTime(DateUtil.date());

        dailyRecordMapper.insert(dailyRecord);
    }
}
