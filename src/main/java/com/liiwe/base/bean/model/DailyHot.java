package com.liiwe.base.bean.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liiwe.base.bean.entity.Topic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author wfli
 * @since 2024/8/20 15:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyHot {

    private int code;
    private String name;
    private String title;
    private String type;
    private String description;
    private String link;
    private int total;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private boolean fromCache;
    private List<Topic> data;

}
