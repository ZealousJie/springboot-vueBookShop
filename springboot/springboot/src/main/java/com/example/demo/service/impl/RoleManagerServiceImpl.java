package com.example.demo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.SearchForm;
import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.CustomException2;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.mapper.PermissionMapper;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.RoleManagerService;
import com.example.demo.service.UserService;
import com.example.demo.utils.BeanCopyUtil;
import com.example.demo.vo.RoleDTO;
import com.example.demo.vo.RoleVO;
import com.example.demo.vo.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Service
public class RoleManagerServiceImpl implements RoleManagerService {
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public PageInfo<RoleVO> findRolesByPage(SearchForm searchForm) {
        List<RoleVO> roleVOList = new ArrayList<>();
        PageHelper.startPage(searchForm.getPage(),searchForm.getRows());
        List<Role> roleList = roleMapper.findRolesByPage("role_name",searchForm.getSearch(), searchForm.getSort(), searchForm.isOrder());
        Map<String,Integer> userNum = new HashMap<>();
        if (roleList != null){
            roleList.forEach(role -> userNum.put(role.getRid(),0));
        }

        findUserRole(userNum);

        if (roleList != null){
            roleList.forEach(role -> {
                ArrayList<Integer> perIdList = permissionMapper.getPerIdByRoleId(role.getRid());
                roleVOList.add(RoleVO.builder()
                        .description(role.getDescription())
                        .roleName(role.getRoleName())
                        .isSystem(role.getIsSystem())
                        .rid(role.getRid())
                        .permissionList(perIdList)
                        .userNumber(userNum.get(role.getRid())).build());

            });
        }

        PageInfo rolePageInfo = new PageInfo<>(roleList);
        rolePageInfo.setList(roleVOList);
        return rolePageInfo;
    }

    @Override
    public PageInfo<RoleDTO> findRoles(SearchForm searchForm) {
        List<RoleDTO> roleDTOList = new ArrayList<>();
        PageHelper.startPage(searchForm.getPage(),searchForm.getRows());
        List<Role> roleList = roleMapper.findRolesByPage("role_name",searchForm.getSearch(), searchForm.getSort(), searchForm.isOrder());
        Map<String,Integer> userNum = new HashMap<>();
        if (roleList != null){
            roleList.forEach(role -> userNum.put(role.getRid(),0));
        }

        findUserRole(userNum);

        if (roleList != null){
            roleList.forEach(role -> {
                ArrayList<Integer> perIdList = permissionMapper.getPerIdByRoleId(role.getRid());
                roleDTOList.add(RoleDTO.builder()
                        .description(role.getDescription())
                        .roleName(role.getRoleName())
                        .isSystem(role.getIsSystem())
                        .rid(Integer.parseInt(role.getRid()))
                        .permissionList(perIdList)
                        .userNumber(userNum.get(role.getRid())).build());

            });
        }

        PageInfo rolePageInfo = new PageInfo<>(roleList);
        rolePageInfo.setList(roleDTOList);
        return rolePageInfo;
    }


    @Override
    public void deleteRoleBatch(List<String> ids) {
        roleMapper.deleteBatchIds(ids);
    }

    @Override
    public List<Permission> findPermission() {
        LambdaQueryWrapper<Permission> wrapper = Wrappers.<Permission>lambdaQuery();
        return permissionMapper.selectList(wrapper);
    }

    @Override
    public void changePermission(String rid, ArrayList<Integer> permissionList,List<String> roleList) {
        ArrayList<Integer> perIdByRoleId = permissionMapper.getPerIdByRoleId(rid);
        if (perIdByRoleId.equals(permissionList)){
            throw new CustomException("请谨慎更改权限!");
        }
        permissionMapper.deleteByRoleId(rid);
        permissionMapper.insertBatch(rid,permissionList);
        roleList.forEach(id -> {
            if (id.equals(rid)) {
                throw new CustomException2("你的账号角色的权限已被修改，10s后强制重新登录");
            }
        });
    }

    @Override
    public List<Integer> getRid() {
        List<Role> roleList = roleMapper.findRolesByPage("role_name", null, null, false);
        List<Integer> roleIdList = new ArrayList<>();
        for (Role role : roleList) {
            roleIdList.add(Integer.parseInt(role.getRid()));
        }
        return roleIdList;
    }

    @Override
    public void insertRole(RoleDTO roleDTO) {
        Role role = BeanCopyUtil.copyProperties(roleDTO, Role.class);
        role.setCreation(new Date());
        roleMapper.insertRole(role);
    }

    @Override
    public void updateRole(RoleVO roleDTO) {
        Role role = Role.builder().rid(roleDTO.getRid()).
                roleName(roleDTO.getRoleName()).isSystem(roleDTO.getIsSystem()).description(roleDTO.getDescription()).build();
        roleMapper.updateByRId(role);
    }

    /**
     * 构建userNum
     * @param userNum
     */
    private void findUserRole(Map<String,Integer> userNum){
        PageInfo<UserVO> users = userService.findUsers(new SearchForm());
        List<UserVO> userVOList = users.getList();
        userVOList.forEach(userVO -> {
            List<Map<String, String>> roles = userVO.getRoles();
            for (Map<String, String> role : roles) {
                if (userNum.containsKey(role.get("id"))){
                    userNum.put(role.get("id"),userNum.get(role.get("id"))+1);
                }
            }
        });
    }
}
