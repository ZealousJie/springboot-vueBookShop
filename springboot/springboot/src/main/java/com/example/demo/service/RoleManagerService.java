package com.example.demo.service;

import com.example.demo.common.SearchForm;
import com.example.demo.entity.Permission;
import com.example.demo.vo.RoleDTO;
import com.example.demo.vo.RoleVO;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface RoleManagerService {

    PageInfo<RoleDTO> findRoles(SearchForm searchForm);
    PageInfo<RoleVO> findRolesByPage(SearchForm searchForm);

    void deleteRoleBatch(List<String> ids);

    List<Permission> findPermission();

    void changePermission(String rid, ArrayList<Integer> permissionList, List<String> roleList);

    List<Integer> getRid();

    void insertRole(RoleDTO roleDTO);
    void updateRole(RoleVO roleDTO);
}
