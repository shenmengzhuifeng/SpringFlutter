package com.tw.cloud.service.impl;

import com.tw.cloud.bean.user.CustomerInfoReply;
import com.tw.cloud.service.UserService;
import com.tw.cloud.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
//        if(!mPasswordEncoder.matches(password,userDetails.getPassword())){
//            throw new BadCredentialsException("密码不正确");
//        }
        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("密码不正确");
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = mJwtTokenUtil.generateToken(userDetails);
        String refreshToken = mJwtTokenUtil.generateRefreshToken(userDetails);
        return new CustomerInfoReply(token,refreshToken,mExpiration,mExpirationRefreshToken);
    }

    @Override
    public String refresh(String refreshToken) {
        return null;
    }
}
