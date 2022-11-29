package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.SearchForm;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.service.RoleManagerService;
import com.example.demo.service.UserService;
import com.example.demo.vo.RoleVO;
import com.example.demo.vo.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                roleVOList.add(RoleVO.builder()
                        .description(role.getDescription())
                        .roleName(role.getRoleName())
                        .isSystem(role.getIsSystem())
                        .rid(role.getRid())
                        .userNumber(userNum.get(role.getRid())).build());
            });
        }

        PageInfo rolePageInfo = new PageInfo<>(roleList);
        rolePageInfo.setList(roleVOList);
        return rolePageInfo;
    }

    /**
     * 构建userNum
     * @param userNum
     */
    private void findUserRole(Map<String,Integer> userNum){
        List<UserVO> userVOList = userService.findUsers(new SearchForm());
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
