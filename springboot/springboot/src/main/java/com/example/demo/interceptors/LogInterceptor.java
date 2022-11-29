package com.example.demo.interceptors;

import cn.hutool.core.util.StrUtil;
import com.example.demo.utils.CookieUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String user = CookieUtil.getCookieValue(request, "user");
        if (StrUtil.isNotBlank(user)){
            return true;
        }
        return false;
    }


}

