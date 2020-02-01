package com.stylefeng.guns.rest.common.enums;

import lombok.Getter;

/**
 * @author : wangwei
 * @date : Created in 2019/11/18 3:25
 * @description: 返回状态枚举类
 * @modified By:
 * @version: v1.0.0
 */
@Getter
public enum BizResponseEnum {

    SUCCESS(0, "成功"),
    FAILD(1, "失败"),
    ERROR(999, "系统出错"),

    ;

    private Integer status;

    private String msg;

    BizResponseEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
