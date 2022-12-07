package com.example.demo.common.dto;

import com.example.demo.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String roleName;
    private String description;
    private Integer isSystem;
    private List<Permission> permissionList;
}
