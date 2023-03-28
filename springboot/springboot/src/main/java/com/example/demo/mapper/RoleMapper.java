package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> findRolesByPage(@Param("column")String column,@Param("search") String search,
                               @Param("sortColumn")String sortColumn,@Param("asc") boolean asc);

    void updateByRId(Role role);

    void insertRole(Role role);
}
