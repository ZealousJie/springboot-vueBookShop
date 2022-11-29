package com.example.demo.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.common.SearchForm;
import com.example.demo.common.exception.CustomException;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.utils.JsonUtil;
import com.example.demo.vo.RegisterVO;
import com.example.demo.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;

    /**
     * 用户账号登录
     * 还要加redis
     * @param user
     * @return
     */
    @Override
    public Result<?> accountLogin(User user) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getAccount, user.getAccount()).eq(User::getPassword, user.getPassword());
        User selectedUser = userMapper.selectOne(wrapper);
        if(selectedUser == null){
            throw new CustomException("用户不存在");
        }
        return Result.success(selectedUser);
    }

    /**
     * 用户注册
     * @param registerVO
     * @return
     */
    @Override
    public Result<?> userRegister(RegisterVO registerVO) {
        boolean flag = identityChecking(registerVO.getIdentityNum());
        if (flag) {
            LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                    .eq(User::getAccount, registerVO.getAccount());
            User selectedUser = userMapper.selectOne(wrapper);
            if(selectedUser != null){
                return Result.error("1","账号已注册");
            }
            LambdaQueryWrapper<User> wrapper1 = Wrappers.<User>lambdaQuery()
                    .eq(User::getIdentityNum, registerVO.getIdentityNum());
            User selectedUser1 = userMapper.selectOne(wrapper1);
            if(selectedUser1 != null){
                return Result.error("1","身份证已被绑定");
            }
            /**
             * 随机uid
             * 默认角色号
             * 默认头像
             * 默认状态2
             */
            List<String> roleIdList = new ArrayList<String>();
            roleIdList.add("79fal8a52e7d49bey3nf8ce90637t536");
            String simpleUUID = IdUtil.fastSimpleUUID();
            User user = User.builder()
                    .account(registerVO.getAccount())
                    .password(registerVO.getPassword())
                    .identityNum(registerVO.getIdentityNum())
                    .realName(registerVO.getRealName())
                    .avatar("C:\\Users\\ZJ\\Desktop\\avatar\\back1.png")
                    .uid(simpleUUID)
                    .state(1)
                    .roleIds(JsonUtil.toJsonStr(roleIdList)).build();
            userMapper.insert(user);
            return Result.success();
        }
        return Result.error("1","注册失败");
    }

    @Override
    public Result<?> insertUser(UserVO userVO) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getUid, getClass());
        User selectedUser = userMapper.selectOne(wrapper);
        String roleIds = selectedUser.getRoleIds();
        List<String> roleList = JsonUtil.fromJsonList(roleIds);
        for (String role : roleList) {
            LambdaQueryWrapper<Role> wrapperRole = Wrappers.<Role>lambdaQuery()
                    .eq(Role::getRid, role);
            Role selectRole = roleMapper.selectOne(wrapperRole);
            Integer isSystem = selectRole.getIsSystem();


            if (isSystem == 1){
                Integer sexNum;
                List<String> roleIdList = new ArrayList<String>();
                roleIdList.add("79fal8a52e7d49bey3nf8ce90637t536");
                String sex = userVO.getSex();
                if (sex.equals("男")){
                     sexNum = 1;
                }else {
                     sexNum = 0;
                }
                User user = User.builder().
                        uid(IdUtil.fastSimpleUUID()).
                        account(userVO.getAccount()).userName(userVO.getUserName())
                        .realName(userVO.getRealName()).password(userVO.getPassword())
                        .sex("sexNum").age(userVO.getAge()).state(2)
                        .roleIds(JsonUtil.toJsonStr(roleIdList)).
                        build();
                userMapper.insert(user);
                return Result.success();
            }else {
                return Result.error("1","非系统用户禁止手动新增用户");
            }
        }
        return Result.error("1","新增失败");
    }

    @Override
    public Result<?> updateUser(UserVO userVO) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getUid, userVO.getAccount());
        User selectedUser = userMapper.selectOne(wrapper);
        String roleIds = selectedUser.getRoleIds();
        List<String> roleList = JsonUtil.fromJsonList(roleIds);
        for (String role : roleList) {
            LambdaQueryWrapper<Role> wrapperRole = Wrappers.<Role>lambdaQuery()
                    .eq(Role::getRid, role);
            Role selectRole = roleMapper.selectOne(wrapperRole);
            Integer isSystem = selectRole.getIsSystem();
            if (isSystem == 1){

                return Result.success();
            }else {
                return Result.error("1","非系统用户禁止手动修改用户");
            }
        }
        return Result.error("1","修改失败");
    }


    @Override
    public List<UserVO> findUsers(SearchForm searchForm) {
        List<UserVO> userVOS = new ArrayList<>();
        List<User> users = userMapper.findUsersAll(searchForm);
        if (users != null){
            users.forEach(user -> {
                //构建角色id-id号 name-角色名键值对
                List<Map<String,String>> roleList = new ArrayList<>(4);
                List<String> roleIds = JsonUtil.fromJsonList(user.getRoleIds());
                if (roleIds != null && roleIds.size()>0){
                    roleIds.forEach(roleId ->{
                        HashMap<String, String> roleMap = new HashMap<>(2);
                        roleMap.put("id",roleId);
                        roleMap.put("name",roleMapper.selectById(roleId).getRoleName());
                        roleList.add(roleMap);
                    });
                }
                //构建userVO
                userVOS.add(UserVO.builder().
                        uid(user.getUid())
                        .account(user.getAccount())
                        .age(user.getAge())
                        .sex(user.getSex())
                        .avatar(user.getAvatar())
                        .creation(user.getCreation())
                        .email(user.getEmail())
                        .identityNum(user.getIdentityNum())
                        .password(user.getPassword())
                        .phone(user.getPhone())
                        .realName(user.getRealName())
                        .roles(roleList)
                        .userName(user.getUserName())
                        .state(user.getState()).build());
            });
        }
        return userVOS;
    }


    /**
     * 身份证校验
     * @param identity
     * @return
     */
    public boolean identityChecking(String identity){
        if (identity.trim().length() != 18){
            return false;
        }
        return true;
    }
}
