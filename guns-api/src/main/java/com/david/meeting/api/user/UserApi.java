package com.david.meeting.api.user;

import com.david.meeting.api.user.vo.UserInfoModel;
import com.david.meeting.api.user.vo.UserModel;

/**
 * @author : wangwei
 * @date : Created in 2019/11/18 1:10
 * @description: 用户接口
 * @modified By:
 * @version: 1.0.0
 */

public interface UserApi {

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    int login(String username, String password);

    /**
     * 用户注册
     * @param userModel
     * @return
     */
    boolean register(UserModel userModel);

    /**
     * 检查用户名
     * @param username
     * @return
     */
    boolean checkUsername(String username);

    /**
     * 获取用户信息
     * @param uuid
     * @return
     */
    UserInfoModel getUserInfo(int uuid);

    /**
     * 修改用户信息
     * @param userInfoModel
     * @return
     */
    UserInfoModel updateUserInfo(UserInfoModel userInfoModel);

}
