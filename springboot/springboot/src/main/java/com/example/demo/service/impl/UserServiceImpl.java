package com.example.demo.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.BuildVo;
import com.example.demo.common.Result;
import com.example.demo.common.SearchForm;
import com.example.demo.common.exception.CustomException;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.mapper.PermissionMapper;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.utils.BeanCopyUtil;
import com.example.demo.utils.CookieUtil;
import com.example.demo.utils.JsonUtil;
import com.example.demo.utils.MD5Util;
import com.example.demo.vo.RegisterVO;
import com.example.demo.vo.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

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
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 用户账号登录
     * 还要加redis 做token
     * @param user
     * @return
     */
    @Override
    public UserVO accountLogin(User user,HttpServletResponse response,HttpServletRequest request) {
        String password = MD5Util.inputPassToFromPass(user.getPassword());
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getAccount, user.getAccount()).eq(User::getPassword, password);
        User selectedUser = userMapper.selectOne(wrapper);
        if(selectedUser == null){
            throw new CustomException("用户不存在或密码错误");
        }
        String roleIds = selectedUser.getRoleIds();
        List<String> roleIdsList = JsonUtil.fromJsonList(roleIds);
        //set集合去重
        Set<Permission> perIdListAll = new HashSet<>();
        if (!roleIdsList.isEmpty()){
            for (String roleId : roleIdsList) {
                //通过roleId查出对应的per_id 循环里有查询 记得优化一下
                List<Permission> perIdList = permissionMapper.getPerByRoleId(roleId);
                perIdListAll.addAll(perIdList);
            }
        }

        //排序
        List<Permission> permissions = perIdListAll.stream().sorted(Comparator.comparing(Permission::getPerId))
                .collect(Collectors.toList());
        UserVO userVO = BuildVo.userVoBuild(permissions, selectedUser,roleIdsList);
        String userTicket = IdUtil.simpleUUID();
        //将用户信息存入redis
        redisTemplate.opsForValue().set("user:"+userTicket,userVO);
        //将标识存入cookie
        CookieUtil.setCookie(request,response,"userTicket",userTicket);
        return userVO;
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
            roleIdList.add("2");
            String simpleUUID = IdUtil.fastSimpleUUID();
            User user = User.builder()
                    .account(registerVO.getAccount())
                    .password(MD5Util.inputPassToFromPass(registerVO.getPassword()))
                    .identityNum(registerVO.getIdentityNum())
                    .realName(registerVO.getRealName())
                    .avatar("https://zjbucket-02.oss-cn-guangzhou.aliyuncs.com/4f73053b481546b2a16325b5e0cb07d4.jpg")
                    .uid(simpleUUID.replace("-",""))
                    .state(1)
                    .roleIds(JsonUtil.toJsonStr(roleIdList)).build();
            userMapper.insert(user);
            return Result.success();
        }
        return Result.error("1","注册失败,不是合法的身份证");
    }

    @Override
    @Transactional
    public Result<?> insertUser(UserVO userVO) {
        //当前操作的用户id
        String uid = userVO.getUid();
        User currentUser = userMapper.selectById(uid);
        String roleIds = currentUser.getRoleIds();
        List<String> roleList = JsonUtil.fromJsonList(roleIds);
        boolean flag = false;
         for (String role : roleList) {
            LambdaQueryWrapper<Role> wrapperRole = Wrappers.<Role>lambdaQuery()
                    .eq(Role::getRid, role);
            Role selectRole = roleMapper.selectOne(wrapperRole);
             Integer isSystem = selectRole.getIsSystem();
             if (isSystem == 1){
                flag = true;
                break;
            }
        }
         try {
             if (flag){
                 User user = User.builder().
                         uid(IdUtil.fastSimpleUUID()).
                         account(userVO.getAccount()).userName(userVO.getUserName())
                         .realName(userVO.getRealName()).password(MD5Util.inputPassToFromPass(userVO.getPassword()))
                         .sex(userVO.getSex()).age(userVO.getAge()).state(2)
                         .roleIds(JsonUtil.toJsonStr(userVO.getRoleIds().stream().map(String::valueOf).collect(Collectors.toList()))).
                                 build();
                 userMapper.insert(user);
                 return Result.success();
             }else {
                 return Result.error("1","非系统用户禁止手动新增用户");
             }
         }catch (Exception e){
             return Result.error("1","新增失败");
         }




    }

    @Override
    public void updateUser(UserVO userVO) {
        List<Integer> roleIds = userVO.getRoleIds();
        userMapper.updateById(User.builder().
                            uid(userVO.getUid())
                            .avatar(userVO.getAvatar())
                            .realName(userVO.getRealName())
                            .identityNum(userVO.getIdentityNum())
                            .state(userVO.getState())
                            .roleIds(JsonUtil.toJsonStr(roleIds.stream().map(String::valueOf).collect(Collectors.toList())))
                            .password(userVO.getPassword())
                            .account(userVO.getAccount())
                            .age(userVO.getAge())
                            .sex(userVO.getSex())
                            .userName(userVO.getUserName())
                            .phone(userVO.getPhone())
                            .email(userVO.getEmail()).build());
    }

    @Override
    public void updateUserPerson(UserVO userVO) {
        userMapper.updateById(User.builder().
                uid(userVO.getUid())
                .avatar(userVO.getAvatar())
                .userName(userVO.getUserName())
                .age(userVO.getAge())
                .sex(userVO.getSex())
                .phone(userVO.getPhone())
                .email(userVO.getEmail()).build());
    }


    @Override
    public PageInfo<UserVO> findUsers(SearchForm searchForm) {
        List<UserVO> userVOS = new ArrayList<>();
        if (searchForm.getPage() != null && searchForm.getRows() != null){
            PageHelper.startPage(searchForm.getPage(),searchForm.getRows());
        }
        searchForm.setColumn("real_name");
        List<User> users = userMapper.findUsersByCondition(searchForm);
        if (users != null){
            users.forEach(user -> {
                //构建角色id-id号 name-角色名键值对
                List<Map<String,String>> roleList = new ArrayList<>(4);
                List<String> roleIds = JsonUtil.fromJsonList(user.getRoleIds());
                List<Integer> roleIdList = new ArrayList<>();
                if (roleIds != null && roleIds.size()>0){
                    roleIds.forEach(roleId ->{
                        roleIdList.add(Integer.parseInt(roleId));
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
                        .roleIds(roleIdList)
                        .state(user.getState()).build());
            });
        }
        PageInfo userVOPageInfo = new PageInfo<>(users);
        userVOPageInfo.setList(userVOS);
        return userVOPageInfo;
    }

    @Override
    public Result<?> getUserByCookie(String userTicket, HttpServletRequest nativeRequest, HttpServletResponse nativeResponse) {
        UserVO user = (UserVO) redisTemplate.opsForValue().get("user:" + userTicket);
        Result<?> result;
        if (user == null) {
           result = Result.success(UserVO.builder().msg("用户授权已过期").build());
        }else {
            result = Result.success(user);
        }
        return result;
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
