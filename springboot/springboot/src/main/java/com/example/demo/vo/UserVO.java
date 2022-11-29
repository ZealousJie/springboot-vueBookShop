package com.example.demo.vo;

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
import java.util.Map;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private List<Map<String,String>> roles;
    private String avatar;
    //ApiModelProperty("用户账号状态(0: 封禁;1: 正常;-1: 未激活)")
    private Integer state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date creation;
}
