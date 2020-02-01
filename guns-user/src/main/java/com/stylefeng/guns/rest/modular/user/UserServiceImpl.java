package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.david.meeting.api.user.UserApi;
import com.david.meeting.api.user.vo.UserInfoModel;
import com.david.meeting.api.user.vo.UserModel;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MeetingTUserMapper;
import com.stylefeng.guns.rest.common.persistence.model.MeetingTUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author : wangwei
 * @date : Created in 2019/11/18 1:21
 * @description: 用户接口实现类
 * @modified By:
 * @version: 1.0.0
 */
@Component
@Service(interfaceClass = UserApi.class, loadbalance = "roundrobin")
public class UserServiceImpl implements UserApi {

    @Autowired
    private MeetingTUserMapper meetingTUserMapper;

    @Override
    public int login(String username, String password) {
        System.out.println("this is user service!" + username +" , "+ password);
        MeetingTUser meetingTUser = new MeetingTUser();
        meetingTUser.setUserName(username);
        MeetingTUser result = meetingTUserMapper.selectOne(meetingTUser);
        if (!StringUtils.isEmpty(result) && result.getUuid() > 0) {
            String md5Password = MD5Util.encrypt(password);
            if (result.getUserPwd().equals(md5Password)) {
                return result.getUuid();
            }
        }
        return 0;
    }

    @Override
    public boolean register(UserModel userModel) {
        // 将注册信息实体转换为数据实体
        MeetingTUser meetingTUser = new MeetingTUser();
        meetingTUser.setUserName(userModel.getUsername());
        meetingTUser.setEmail(userModel.getEmail());
        meetingTUser.setAddress(userModel.getAddress());
        meetingTUser.setUserPhone(userModel.getPhone());
        // 创建时间和修改时间 -> current_timestamp

        // 密码加密【MD5混淆加密 + 盐值(针对每一个用户随机生成一个盐，当前密码+盐值加密，更加安全)】
        String md5Password = MD5Util.encrypt(userModel.getPassword());
        meetingTUser.setUserPwd(md5Password);// 注意
        // 将数据实体保存
        Integer num = meetingTUserMapper.insert(meetingTUser);
        if (num > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkUsername(String username) {
        EntityWrapper<MeetingTUser> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name", username);
        Integer count = meetingTUserMapper.selectCount(entityWrapper);
        if (count != null && count > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public UserInfoModel getUserInfo(int uuid) {

        MeetingTUser meetingTUser = meetingTUserMapper.selectById(uuid);
        UserInfoModel userInfoModel = do2UserInfo(meetingTUser);
        return userInfoModel;
    }

    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {
        // 将传入的参数转换为DO 【MeetingTUser】
        MeetingTUser meetingTUser = new MeetingTUser();
        meetingTUser.setUuid(userInfoModel.getUuid());
        meetingTUser.setNickName(userInfoModel.getNickname());
        meetingTUser.setLifeState(Integer.parseInt(userInfoModel.getLifeState()));
        meetingTUser.setBirthday(userInfoModel.getBirthday());
        meetingTUser.setBiography(userInfoModel.getBiography());
        meetingTUser.setBeginTime(null);
        meetingTUser.setHeadUrl(userInfoModel.getHeadAddress());
        meetingTUser.setEmail(userInfoModel.getEmail());
        meetingTUser.setAddress(userInfoModel.getAddress());
        meetingTUser.setUserPhone(userInfoModel.getPhone());
        meetingTUser.setUserSex(userInfoModel.getSex());
        meetingTUser.setUpdateTime(null);

        // DO存入数据库
        Integer integer = meetingTUserMapper.updateById(meetingTUser);
        if(integer>0){
            // 将数据从数据库中读取出来
            UserInfoModel userInfo = getUserInfo(meetingTUser.getUuid());
            // 将结果返回给前端
            return userInfo;
        }else{
            return null;
        }

    }

    private UserInfoModel do2UserInfo(MeetingTUser meetingTUser) {
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setUuid(meetingTUser.getUuid());
        userInfoModel.setUsername(meetingTUser.getUserName());
        userInfoModel.setUpdateTime(meetingTUser.getUpdateTime().getTime());
        userInfoModel.setSex(meetingTUser.getUserSex());
        userInfoModel.setPhone(meetingTUser.getUserPhone());
        userInfoModel.setNickname(meetingTUser.getNickName());
        userInfoModel.setLifeState(meetingTUser.getLifeState() + "");
        userInfoModel.setHeadAddress(meetingTUser.getHeadUrl());
        userInfoModel.setEmail(meetingTUser.getEmail());
        userInfoModel.setBirthday(meetingTUser.getBirthday());
        userInfoModel.setBiography(meetingTUser.getBiography());
        userInfoModel.setBeginTime(meetingTUser.getBeginTime().getTime());
        userInfoModel.setAddress(meetingTUser.getAddress());

        return userInfoModel;
    }
}
