package com.tw.cloud.service.impl;

import com.tw.cloud.bean.user.CustomerInfoReply;
import com.tw.cloud.bean.user.User;
import com.tw.cloud.mapper.UserMapper;
import com.tw.cloud.service.UserService;
import com.tw.cloud.utils.JwtTokenUtil;
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

    @Override
    public String register() {
        return null;
    }

    @Override
    public CustomerInfoReply login(String username, String password) {
        UserDetails userDetails = mUserDetailsService.loadUserByUsername(username);
        if(!mPasswordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("密码不正确");
        }
//        if(!password.equals(userDetails.getPassword())){
//            throw new BadCredentialsException("密码不正确");
//        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = mJwtTokenUtil.generateToken(userDetails);
        String refreshToken = mJwtTokenUtil.generateRefreshToken(userDetails);
        return new CustomerInfoReply(token,refreshToken,mExpiration,mExpirationRefreshToken);
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
        int count = mUserMapper.insert(umsAdmin);
        return null;
    }

    @Override
    public void updateUserHeader(String headUrl) {
        UserDetails user = getUserDetails();

        mUserMapper.updateUserHeader(user.getUsername(),headUrl);
    }

    @Override
    public User getCustomerInfo() {
        UserDetails user = getUserDetails();
        return mUserMapper.selectUserByLoginName(user.getUsername());
    }

    @Override
    public UserDetails getUserDetails() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return userDetails;
    }

    @Override
    public String refresh(String refreshToken) {
        return null;
    }
}
