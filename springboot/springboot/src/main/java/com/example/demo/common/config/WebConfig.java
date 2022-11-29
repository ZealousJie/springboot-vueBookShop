package com.example.demo.common.config;

import com.example.demo.interceptors.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LogInterceptor())
//                .excludePathPatterns("/user/accountLogin","/user/emailLogin","/user/email/{email}","/user/register");
//
//    }
}
