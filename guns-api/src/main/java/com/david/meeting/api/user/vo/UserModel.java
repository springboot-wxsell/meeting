package com.david.meeting.api.user.vo;

import lombok.Data;

import java.io.Serializable;
/**
 * @author : wangwei
 * @date : Created in 2019/11/18 1:21
 * @description: 用户注册页面实体类
 * @modified By:
 * @version: 1.0.0
 */
@Data
public class UserModel implements Serializable {

    private static final long serialVersionUID = 8296068271682042039L;

    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;

}
