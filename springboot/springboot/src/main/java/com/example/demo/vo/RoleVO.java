package com.example.demo.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleVO {

    private String rid;
    private String roleName;
    private Integer isSystem;
    private String description;
    private Integer userNumber;

}

