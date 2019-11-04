package com.tw.cloud.bean.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 安全验证的user类
 *
 * @author
 * @create 2019-11-02 3:28 PM
 **/
public class JwtUserDetail implements UserDetails {

    private User mUser;


    public JwtUserDetail(User user){
        this.mUser = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return mUser.getPassword();
    }

    @Override
    public String getUsername() {
        return mUser.getLoginName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
