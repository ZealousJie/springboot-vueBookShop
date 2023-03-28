package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.common.SearchForm;
import com.example.demo.entity.User;
import com.example.demo.vo.RegisterVO;
import com.example.demo.vo.UserVO;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.formula.functions.T;
import org.apache.xmlgraphics.io.Resource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */

public interface UserService {
    UserVO accountLogin(User user,HttpServletResponse response,HttpServletRequest request);
    Result<?> userRegister(RegisterVO registerVO);
    Result<?> insertUser(UserVO user);
    void updateUser(UserVO user);
    void updateUserPerson(UserVO userVo);
    PageInfo<UserVO> findUsers(SearchForm searchForm);
    Result<?> getUserByCookie(String userTicket, HttpServletRequest nativeRequest, HttpServletResponse nativeResponse);
}
