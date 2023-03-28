package com.example.demo.interceptors;

import cn.hutool.core.util.StrUtil;
import com.example.demo.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Component
@Slf4j
public class LogInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userTicket = CookieUtil.getCookieValue(request, "userTicket");
        if (StrUtil.isBlank(userTicket)){
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print("{\"success\":false,\"msg\":\"NoUser\"}");
            return false;
        }
        return true;
    }


}

