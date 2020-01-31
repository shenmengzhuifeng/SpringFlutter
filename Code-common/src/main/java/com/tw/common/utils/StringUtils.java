package com.tw.common.utils;

import java.util.Random;

/**
 * 字符串相关方法类
 *
 * @author
 * @create 2020-01-31 12:41 PM
 **/
public class StringUtils {


    /**
     * 生成6位随机码
     * @return
     */
    public static String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
