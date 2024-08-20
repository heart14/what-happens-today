package com.liiwe.base.bean.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wfli
 * @since 2024/8/20 15:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResult {

    private int httpStatus;

    private String httpResult;
}
