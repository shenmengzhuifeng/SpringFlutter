package com.tw.cloud.service.impl;

import com.tw.cloud.bean.CommonResp;
import com.tw.cloud.bean.user.CustomerInfoReply;
import com.tw.cloud.bean.user.User;
import com.tw.cloud.controller.UserController;
import com.tw.cloud.mapper.UserMapper;
import com.tw.cloud.service.RedisService;
import com.tw.cloud.service.UserService;
import com.tw.cloud.utils.JwtTokenUtil;
import com.tw.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户服务实现类
 *
 * @author
 * @create 2019-11-01 9:46 PM
 **/
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private UserDetailsService mUserDetailsService;

    @Autowired
    private PasswordEncoder mPasswordEncoder;

    @Autowired
    private JwtTokenUtil mJwtTokenUtil;

    @Autowired
    private UserMapper mUserMapper;


    @Value("${jwt.expiration}")
    private Long mExpiration;
    @Value("${jwt.expirationRefreshToken}")
    private Long mExpirationRefreshToken;

    @Autowired
    private RedisService redisService;
    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.key.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;


    @Override
    public String register() {
        return null;
    }

    @Override
    public CustomerInfoReply login(String username, String password) {
        UserDetails userDetails = mUserDetailsService.loadUserByUsername(username);
        if (!mPasswordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("密码不正确");
        }
//        if(!password.equals(userDetails.getPassword())){
//            throw new BadCredentialsException("密码不正确");
//        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = mJwtTokenUtil.generateToken(userDetails);
        String refreshToken = mJwtTokenUtil.generateRefreshToken(userDetails);
        return new CustomerInfoReply(token, refreshToken, mExpiration, mExpirationRefreshToken);
    }

    @Override
    public CustomerInfoReply register(String username, String password) {
        User umsAdmin = new User();
        umsAdmin.setLoginName(username);
        umsAdmin.setCreateTime(new Date());
        //TODO 查询是否有相同用户名的用户
        //将密码进行加密操作
        String encodePassword = mPasswordEncoder.encode(password);
        umsAdmin.setPassword(encodePassword);
//        int count = mUserMapper.insert(umsAdmin);
        return null;
    }

    @Override
    public void updateUserHeader(String headUrl) {
        UserDetails user = getUserDetails();

//        mUserMapper.updateUserHeader(user.getUsername(), headUrl);
    }

    @Override
    public User getCustomerInfo() {
        UserDetails user = getUserDetails();
        return null;
//        return mUserMapper.selectUserByLoginName(user.getUsername());
    }

    @Override
    public UserDetails getUserDetails() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return userDetails;
    }

    @Override
    public void sendMobileCode(String mobilePhone) {
        String code = StringUtils.randomCode();
        LOGGER.info("MobilePhone:" + mobilePhone + "---code==" + code);
        //验证码绑定手机号并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + mobilePhone, code);
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + mobilePhone, AUTH_CODE_EXPIRE_SECONDS);
    }


    //对输入的验证码进行校验
    @Override
    public CommonResp verifyAuthCode(String telephone, String authCode) {
        if (org.springframework.util.StringUtils.isEmpty(authCode)) {
            return CommonResp.failed("请输入验证码");
        }
        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
        boolean result = authCode.equals(realAuthCode);
        if (result) {
            return CommonResp.success(null, "验证码校验成功");
        } else {
            return CommonResp.failed("验证码不正确");
        }
    }

    @Override
    public String refresh(String refreshToken) {
        return null;
    }
}
