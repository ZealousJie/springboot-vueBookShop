package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Team;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface TeamMapper extends BaseMapper<Team> {


    List<Team> queryByIds(@Param("list") List<Integer> teamList);
}
