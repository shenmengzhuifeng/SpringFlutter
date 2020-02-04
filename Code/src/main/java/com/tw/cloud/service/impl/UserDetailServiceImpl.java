package com.tw.cloud.service.impl;

import com.tw.cloud.bean.user.JwtUserDetail;
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

    /**
     * 此处username直接使用用户id
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.tw.cloud.bean.User user = mUserMapper.selectByPrimaryKey(Integer.parseInt(username));
        if (user != null) {
            return new JwtUserDetail(user);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}
