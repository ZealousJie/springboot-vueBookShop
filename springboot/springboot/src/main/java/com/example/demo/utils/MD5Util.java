package com.example.demo.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Component
public class MD5Util {

    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    private static final String salt = "1a2b3c4d";

    /**
     * 第一次加密 前端
     *
     * @param inputPass
     * @return java.lang.String
     * @operation add
     * @date 4:49 下午 2022/3/1
     **/
    public static String inputPassToFromPass(String inputPass) {
        String str = ""+salt.charAt(0) + salt.charAt(3) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * 第二次加密
     * @operation add
     * @date 4:52 下午 2022/3/1
     * @param formPass
     * @param salt
     * @return java.lang.String
     **/
    public static String formPassToDBPass(String formPass, String salt) {
        String str = ""+salt.charAt(0) + salt.charAt(3) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt) {
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = formPassToDBPass(fromPass, salt);
        return dbPass;
    }

    /**
     * 测试验证
     * @param args
     */
    public static void main(String[] args) {
        String s = inputPassToFromPass("123456");
        System.out.println(s);
        System.out.println(formPassToDBPass(s, salt));
        System.out.println(inputPassToDBPass("123456",salt));
    }


}