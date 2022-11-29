package com.example.demo.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.rmi.CORBA.Util;
import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public class JsonUtil {


    /**
     * 对象转Json字符串
     * @param obj
     * @return
     */
    public static String toJsonStr(Object obj){
        if (obj instanceof String){
            return Utils.toNotNullString(obj);
        }
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * json字符串转换为List对象
     * @param jsonStr
     * @return
     */
    public static <T> List<T> fromJsonList(String jsonStr){
        Gson gson = new Gson();
        TypeToken<List<T>> typeToken = new TypeToken<List<T>>(){};

        return gson.fromJson(jsonStr,typeToken.getType());
    }
}
