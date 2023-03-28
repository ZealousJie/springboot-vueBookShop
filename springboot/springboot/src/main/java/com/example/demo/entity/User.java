package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户实体类
 */
@TableName("match_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    public interface LoginGroup{}

    private static final long serialVersionUID = 1L;

    @TableId
    private String uid;
    private String userName;
    private String realName;
    private String password;

    private String account;
    @Pattern(regexp = "^1(3\\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$",message = "手机格式不正确")
    private String phone;
    @Email
    private String email;
    private Integer age;
    private String sex;
    private String identityNum;
    private String roleIds;
    private String avatar;
    //ApiModelProperty("用户账号状态(0: 封禁;1: 正常)")
    private Integer state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date creation;
}
