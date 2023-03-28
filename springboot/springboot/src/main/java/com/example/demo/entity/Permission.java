package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 资源表实体
 * @ author zealousJie
 * @ version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("match_permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Integer perId;
    private String name;
    private String path;
    private String comment;
    private String icon;

}
