package com.stylefeng.guns.rest.common.utils;

import java.util.UUID;

/**
 * @author : wangwei
 * @date : Created in 2020/2/23 20:35
 * @description: ${description}
 * @modified By:
 * @version: ${version}
 */
public class UuidUtils {

    public static String genUuid() {
        return UUID.randomUUID().toString();
    }
}
