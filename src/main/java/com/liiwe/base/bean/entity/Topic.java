package com.liiwe.base.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wfli
 * @since 2024/8/20 15:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    private String title;
    private String desc;
    private String timestamp;
    private long hot;
    private String url;
    private String mobileUrl;

}
