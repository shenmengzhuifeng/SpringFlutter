package com.tw.cloud.bean.user;

/**
 * 登录，刷新token等返回的认证信息
 *
 * @author
 * @create 2019-11-03 8:11 PM
 **/
public class CustomerInfoReply {

    private int reply = 1;//0：实名 1：匿名

    private String token;

    private String refreshToken;

    private Long tokenExpireTime;

    private Long refreshTokenExpireTime;

    private String mobilePhone;

    public CustomerInfoReply(int reply,String token, String refreshToken,
                             Long tokenExpireTime, Long refreshTokenExpireTime,
                             String mobilePhone) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.tokenExpireTime = tokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
        this.reply = reply;
        this.mobilePhone = mobilePhone;
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

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
