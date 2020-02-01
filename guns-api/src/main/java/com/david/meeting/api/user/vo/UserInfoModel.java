package com.david.meeting.api.user.vo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author : wangwei
 * @date : Created in 2019/11/18 1:21
 * @description: 用户中心页面实体类
 * @modified By:
 * @version: 1.0.0
 */
@Data
public class UserInfoModel implements Serializable {

    private static final long serialVersionUID = 5202068556223162664L;

    private Integer uuid;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private Integer sex;
    private String birthday;
    private String lifeState;
    private String biography;
    private String address;
    private String headAddress;
    private long beginTime;
    private long updateTime;

}
