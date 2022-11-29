package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.common.SearchForm;
import com.example.demo.entity.User;
import com.example.demo.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    int deleteByUid(String uid);
    List<User> findUsersAll(SearchForm searchForm);
}
