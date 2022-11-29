package com.example.demo.utils;

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
}
