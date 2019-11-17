package com.tw.cloud.controller;

import cn.hutool.json.JSONUtil;
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
import org.springframework.web.bind.annotation.*;
import com.tw.cloud.bean.CommonResp;
import com.tw.cloud.utils.Constants;
import com.tw.cloud.utils.UnifyApiUri;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
    @Value("${file.UPLOADED_FOLDER}")
    private String UPLOADED_FOLDER;

    @RequestMapping(value = UnifyApiUri.UserApi.API_CUSTOMER_INFO, method = RequestMethod.GET)
    public CommonResp<User> getCustomerInfo(HttpServletRequest request) {
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            String authToken = authHeader.substring(this.tokenHead.length());// The part after "Bearer "
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            User user = userMapper.selectUserByLoginName(username);
            return CommonResp.success(user);
        } else {
            return new CommonResp<User>(Constants.RESULT_ERROR, ResultCode.RESULT_CODE_1002.getCode()
                    , ResultCode.RESULT_CODE_1002.getMessage(), null);
        }
    }

    @RequestMapping(value = UnifyApiUri.UserApi.API_AUTH_LOGIN,
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResp<CustomerInfoReply> login(@RequestBody AuthenticationRequest authenticationRequest) {
        LOGGER.info("login info==>" + authenticationRequest.toString());
        CustomerInfoReply customerInfoReply = mUserService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        LOGGER.info("customerInfoReply info==>" + customerInfoReply.toString());
        return CommonResp.success(customerInfoReply);
    }

    @RequestMapping(value = UnifyApiUri.UserApi.API_AUTH_REGISTER,
            method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResp<CustomerInfoReply> register(@RequestBody AuthenticationRequest authenticationRequest) {
        LOGGER.info("login info==>" + authenticationRequest.toString());
        CustomerInfoReply customerInfoReply = mUserService.register(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        LOGGER.info("customerInfoReply info==>" + customerInfoReply.toString());
        return CommonResp.success(customerInfoReply);
    }

    @RequestMapping(value = UnifyApiUri.UserApi.API_UPDATE_CUSTOMER_INFO,
            method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateCustomerInfo(@RequestParam("file") MultipartFile file,HttpServletRequest request) {
        if (file.isEmpty()){
            return JSONUtil.parse(CommonResp.failed("file can not null")).toStringPretty();
        }
        // Get the file and save it somewhere
        try {
            byte[] bytes = file.getBytes();
            String tempUrl = UUID.randomUUID() + file.getOriginalFilename();
            String pathString = UPLOADED_FOLDER + tempUrl;
            Path path = Paths.get(pathString);
            Files.write(path, bytes);
            mUserService.updateUserHeader(jwtTokenUtil.getLoginNameFromHttpServer(request),tempUrl);
            return JSONUtil.parse(CommonResp.success("result is ok")).toStringPretty();
        } catch (IOException e) {
            e.printStackTrace();
            return JSONUtil.parse(CommonResp.failed(e.getMessage())).toStringPretty();
        }
    }
}
