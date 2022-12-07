package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    List<String> getPerIdByRoleId(@Param("roleId") String roleId);
}
