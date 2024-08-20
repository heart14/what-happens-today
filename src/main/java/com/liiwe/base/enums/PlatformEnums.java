package com.liiwe.base.enums;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wfli
 * @since 2024/8/20 16:21
 */
@Getter
public enum PlatformEnums {

    BAIDU("baidu", "百度"),
    WEIBO("weibo", "微博"),
    TOUTIAO("toutiao", "今日头条"),
    DOUYIN("douyin", "抖音");

    private final String name;
    private final String desc;

    PlatformEnums(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public static List<String> nameList() {
        List<String> nameList = new ArrayList<>();
        for (PlatformEnums platform : PlatformEnums.values()) {
            nameList.add(platform.getName());
        }
        return nameList;
    }
}
