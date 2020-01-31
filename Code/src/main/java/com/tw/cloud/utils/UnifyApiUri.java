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
        public static final String API_CUSTOMER_INFO = API_CUSTOMER_BASE + "/getCustomerInfo";
        public static final String API_UPDATE_CUSTOMER_INFO = API_CUSTOMER_BASE + "/updateCustomerInfo";



        public static final String API_AUTH_BASE = "/auth";
        public static final String API_AUTH_LOGIN = API_AUTH_BASE + "/login";
        public static final String API_AUTH_REGISTER = API_AUTH_BASE + "/register";
        public static final String API_AUTH_SEND_MOBILE_PHONE_CODE = API_AUTH_BASE + "/sendMobileCode";
        public static final String API_AUTH_LOGIN_BY_PHONE = API_AUTH_BASE + "/login_by_phone";
        public static final String API_AUTH_REFRESH_TOKEN = API_AUTH_BASE + "/refresh_token";
    }
}
