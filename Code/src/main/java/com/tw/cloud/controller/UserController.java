package com.tw.cloud.controller;

import com.tw.cloud.bean.request.AuthenticationRequest;
import com.tw.cloud.bean.user.CustomerInfoReply;
import com.tw.cloud.bean.user.User;
import com.tw.cloud.component.JwtAuthenticationTokenFilter;
import com.tw.cloud.mapper.UserMapper;
import com.tw.cloud.service.UserService;
import com.tw.cloud.utils.JwtTokenUtil;
import com.tw.cloud.utils.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.tw.cloud.bean.CommonResp;
import com.tw.cloud.utils.Constants;
import com.tw.cloud.utils.UnifyApiUri;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @RequestMapping(value = UnifyApiUri.UserApi.API_CUSTOMER_INFO,method = RequestMethod.GET)
    public CommonResp<User> getCustomerInfo(HttpServletRequest request) {
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            String authToken = authHeader.substring(this.tokenHead.length());// The part after "Bearer "
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            User user = userMapper.selectUserByLoginName(username);
            return CommonResp.success(user);
        }else {
            return new CommonResp<User>(Constants.RESULT_ERROR,ResultCode.RESULT_CODE_1002.getCode()
            ,ResultCode.RESULT_CODE_1002.getMessage(),null);
        }
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
