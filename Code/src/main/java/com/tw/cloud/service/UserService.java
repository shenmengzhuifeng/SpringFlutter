package com.tw.cloud.service;

import com.tw.cloud.bean.user.CustomerInfoReply;

/**
 * 用户相关操作
 *
 * @author
 * @create 2019-11-02 8:45 PM
 **/
public interface UserService {

    String register();

    CustomerInfoReply login(String username, String password);

    //刷新token
    String refresh(String refreshToken);
}
