package com.tw.cloud.service.impl;

import com.tw.cloud.bean.user.JwtUserDetail;
import com.tw.cloud.bean.user.User;
import com.tw.cloud.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户服务类
 *
 * @author
 * @create 2019-11-02 8:42 PM
 **/
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper mUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = mUserMapper.selectUserByLoginName(username);
        User user = null;
        if (user != null) {
            return new JwtUserDetail(user);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}
