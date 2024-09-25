package com.liiwe.base.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wfli
 * @since 2024/9/25 16:25
 */
@TableName("t_daily_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecord {

    private Long id;

    private Date date;

    private String title;

    private String amount;

    private String type;

    private String category;

    private String desc;

    private Date createTime;

    private Date updateTime;

    private String username;
}
