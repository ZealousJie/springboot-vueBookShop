package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Integer rid;
    private String roleName;
    private Integer isSystem;
    private String description;
    private Integer userNumber;
    private ArrayList<Integer> permissionList;
    private List<String> currentRoleIds;
}
