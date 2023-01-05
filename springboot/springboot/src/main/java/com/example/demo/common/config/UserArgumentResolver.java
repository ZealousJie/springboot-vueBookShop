package com.example.demo.common.config;

import cn.hutool.core.util.StrUtil;
import com.example.demo.common.Result;
import com.example.demo.common.annotation.LoginUser;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 配置自定义用户(TUser)参数  再配置springmvc 即WebConfig
 */
@Component
@Slf4j
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Resource
    private UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(LoginUser.class)){
            return User.class.isAssignableFrom(parameter.getParameterType());
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        //现在是直接将用户的数据存到cookie里
        //这里是将登录用户的数据存入redis 然后生成一个userTicket随机数字符串 作为Key存入cooKie
        //由于每次跑项目都要启动redis太麻烦了 就先把代码思路写在这
        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse nativeResponse = webRequest.getNativeResponse(HttpServletResponse.class);
        String userTicket = CookieUtil.getCookieValue(nativeRequest, "userTicket");
        if (!StrUtil.isNotBlank(userTicket)) {
            return Result.error("0","您未登录");
        }
        Result result;
        //改方法待写
        try {
             result = userService.getUserByCookie(userTicket, nativeRequest, nativeResponse);
        } catch (Exception e) {
            result = Result.error("0","服务器异常");
            log.warn(e.getLocalizedMessage());
        }
        return result;
    }

}
