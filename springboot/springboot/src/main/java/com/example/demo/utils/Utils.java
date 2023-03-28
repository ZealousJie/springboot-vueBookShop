package com.example.demo.utils;

import java.util.Random;

/**
 * 通用工具类
 * @ author zealousJie
 * @ version 1.0
 */
public class Utils {

    /**
     * 转非空字符串
     * @param obj
     * @return
     */
    public static String toNotNullString(Object obj){

        return obj == null ? "" : obj.toString();
    }

    public static String randomMaker(int len){
        String source = "0123456789";
        Random random = new Random();
        StringBuilder rs = new StringBuilder();
        for (int i = 0; i < len; i++) {
            rs.append(source.charAt(random.nextInt(10)));
        }
        return rs.toString();
    }
}
