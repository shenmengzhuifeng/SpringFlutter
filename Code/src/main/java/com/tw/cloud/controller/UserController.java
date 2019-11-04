package com.tw.cloud.controller;

import com.tw.cloud.bean.request.AuthenticationRequest;
import com.tw.cloud.bean.user.CustomerInfoReply;
import com.tw.cloud.component.JwtAuthenticationTokenFilter;
import com.tw.cloud.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.tw.cloud.bean.CommonResp;
import com.tw.cloud.utils.Constants;
import com.tw.cloud.utils.UnifyApiUri;

/**
 * 用户相关controller
 *
 * @author
 * @create 2019-10-20 7:50 PM
 **/
@RestController
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService mUserService;

    @RequestMapping(value = UnifyApiUri.UserApi.API_CUSTOMER_INFO,method = RequestMethod.GET)
    public CommonResp<String> getCustomerInfo() {
        return new CommonResp<>(Constants.RESULT_OK,"","","获取成功");
    }

    @RequestMapping(value = UnifyApiUri.UserApi.API_AUTH_LOGIN,
            method = RequestMethod.POST,produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResp<CustomerInfoReply> login(@RequestBody AuthenticationRequest authenticationRequest) {
        LOGGER.info("login info==>" + authenticationRequest.toString());
        CustomerInfoReply customerInfoReply = mUserService.login(authenticationRequest.getUsername(),authenticationRequest.getPassword());
        LOGGER.info("customerInfoReply info==>" + customerInfoReply.toString());
        return CommonResp.success(customerInfoReply);
    }
}
