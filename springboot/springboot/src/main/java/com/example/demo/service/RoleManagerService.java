package com.example.demo.service;

import com.example.demo.common.SearchForm;
import com.example.demo.vo.RoleVO;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface RoleManagerService {

    PageInfo<RoleVO> findRolesByPage(SearchForm searchForm);

    void deleteRoleBatch(List<String> ids);
}
