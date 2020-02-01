package com.stylefeng.guns.rest.common;


import com.david.meeting.api.user.vo.UserInfoModel;

/**
 * @author : wangwei
 * @date : Created in 2019/11/24 0:45
 * @description: 当前用户
 * @modified By:
 * @version: v1.0.0
 */
public class CurrentUser {

    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    public static void saveUserId(String userId) {
        THREAD_LOCAL.set(userId);
    }

    public static String getCurrentUser() {
        return THREAD_LOCAL.get();
    }

//    每个线程都会存储一份用户信息, 可以导致内存不足
//    /**
//     * 线程绑定的存储空间
//     */
//    private static final ThreadLocal<UserInfoModel> THREAD_LOCAL = new ThreadLocal<>();
//
//    /**
//     * 将用户信息放入存储空间
//     *
//     * @param userInfoModel
//     */
//    public static void saveUserInfo(UserInfoModel userInfoModel) {
//        THREAD_LOCAL.set(userInfoModel);
//    }
//
//    /**
//     * 将用户信息取出
//     *
//     * @return
//     */
//    public static UserInfoModel getCurrentUser() {
//        return THREAD_LOCAL.get();
//    }
//
//    /**
//     * 清除用户信息
//     */
//    public static void clearCurrentUser() {
//        THREAD_LOCAL.remove();
//    }
}
