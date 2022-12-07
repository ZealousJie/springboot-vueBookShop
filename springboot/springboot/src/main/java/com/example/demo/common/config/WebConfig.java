package com.example.demo.common.config;

import com.example.demo.interceptors.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private UserArgumentResolver userArgumentResolver;

// 一个开关 你懂的
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(userArgumentResolver);
//    }
}
