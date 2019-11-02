package com.tw.cloud.utils;

/**
 * 所有接口地址类
 *
 * @author
 * @create 2019-10-20 7:55 PM
 **/
public class UnifyApiUri {

    public static class UserApi {
        public static final String API_CUSTOMER_BASE = "/user";
        public static final String API_CUSTOMER_INFO = "/getCustomerInfo";



        public static final String API_AUTH_BASE = "/auth";
        public static final String API_AUTH_LOGIN = API_AUTH_BASE + "/login";
        public static final String API_AUTH_REFRESH_TOKEN = API_AUTH_BASE + "/refresh_token";
    }
}