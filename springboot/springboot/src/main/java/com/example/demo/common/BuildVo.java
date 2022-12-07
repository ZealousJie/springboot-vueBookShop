package com.example.demo.common;

import com.example.demo.entity.Permission;
import com.example.demo.entity.User;
import com.example.demo.vo.UserVO;

import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public class BuildVo {
    public static UserVO userVoBuild(List<Permission> permissionList, User selectedUser){
        UserVO userVO = UserVO.builder()
                .permissionList(permissionList)
                .state(selectedUser.getState())
                .userName(selectedUser.getUserName())
                .phone(selectedUser.getPhone())
                .sex(selectedUser.getSex())
                .password(selectedUser.getPassword())
                .email(selectedUser.getEmail())
                .creation(selectedUser.getCreation())
                .age(selectedUser.getAge())
                .account(selectedUser.getAccount())
                .identityNum(selectedUser.getIdentityNum())
                .uid(selectedUser.getUid())
                .avatar(selectedUser.getAvatar())
                .realName(selectedUser.getRealName())
                .build();
        return userVO;
    }
}
