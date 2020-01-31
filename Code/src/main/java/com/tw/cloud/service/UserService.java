package com.tw.cloud.service;

import com.tw.cloud.bean.CommonResp;
import com.tw.cloud.bean.user.CustomerInfoReply;
import com.tw.cloud.bean.user.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户相关操作
 *
 * @author
 * @create 2019-11-02 8:45 PM
 **/
public interface UserService {

    String register();

    CustomerInfoReply login(String username, String password);

    CustomerInfoReply register(String username, String password);

    void updateUserHeader(String headUrl);

    User getCustomerInfo();

    UserDetails getUserDetails();


    void sendMobileCode(String mobilePhone);

    CommonResp verifyAuthCode(String telephone, String authCode);

    //刷新token
    String refresh(String refreshToken);
}
