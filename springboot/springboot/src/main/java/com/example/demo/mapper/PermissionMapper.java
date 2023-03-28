package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    ArrayList<Integer> getPerIdByRoleId(@Param("roleId") String roleId);
    List<Permission> getPerByRoleId(@Param("roleId") String roleId);
    int deleteByRoleId(@Param("roleId") String roleId);

    int insertBatch(@Param("roleId") String roleId,@Param("perList") ArrayList<Integer> perList);
}
