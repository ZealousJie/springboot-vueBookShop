package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.common.SearchForm;
import com.example.demo.entity.User;
import com.example.demo.vo.RegisterVO;
import com.example.demo.vo.UserVO;
import org.apache.poi.ss.formula.functions.T;
import org.apache.xmlgraphics.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */

public interface UserService {
    Result<?> accountLogin(User user);
    Result<?> userRegister(RegisterVO registerVO);
    Result<?> insertUser(UserVO user);
    Result<?> updateUser(UserVO user);
    List<UserVO> findUsers(SearchForm searchForm);
}
