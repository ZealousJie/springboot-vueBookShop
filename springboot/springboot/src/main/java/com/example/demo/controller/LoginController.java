package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Resource
    UserService userService;

    @PostMapping("/accountLogin")//
    public Result<?> accountLogin(@RequestBody User user, HttpServletResponse response, HttpServletRequest request){
        boolean flag = checking(user.getAccount(),user.getPassword());
        if (flag){
            try{
                UserVO accountLogin = userService.accountLogin(user,response,request);
                return Result.success(accountLogin);
            }catch (Exception e){
                log.warn(e.toString());
                return Result.error("1",e.toString());
            }
        }
        return Result.error("1","账号或密码不规范");

    }


    public boolean checking(String account,String pwd){
        if (account.length()<11 && StrUtil.isNotBlank(account)){
            return true;
        }
        if (pwd.length()<33 && StrUtil.isNotBlank(pwd)){
            return true;
        }
        return false;
    }
}
