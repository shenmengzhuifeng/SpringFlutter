package com.tw.cloud.bean.user;

/**
 * 登录，刷新token等返回的认证信息
 *
 * @author
 * @create 2019-11-03 8:11 PM
 **/
public class CustomerInfoReply {

    private String token;

    private String refreshToken;

    private Long tokenExpireTime;

    private Long refreshTokenExpireTime;

    public CustomerInfoReply(String token, String refreshToken, Long tokenExpireTime, Long refreshTokenExpireTime) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.tokenExpireTime = tokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(Long tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    public Long getRefreshTokenExpireTime() {
        return refreshTokenExpireTime;
    }

    public void setRefreshTokenExpireTime(Long refreshTokenExpireTime) {
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    @Override
    public String toString() {
        return "CustomerInfoReply{" +
                "token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", tokenExpireTime=" + tokenExpireTime +
                ", refreshTokenExpireTime=" + refreshTokenExpireTime +
                '}';
    }
}
