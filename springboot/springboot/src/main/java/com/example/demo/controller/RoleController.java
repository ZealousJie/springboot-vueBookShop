package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.common.SearchForm;
import com.example.demo.common.dto.IdListDto;
import com.example.demo.service.RoleManagerService;
import com.example.demo.vo.RoleVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@RestController
@RequestMapping("/role")
@CrossOrigin
@Slf4j
public class RoleController {

    @Resource
    private RoleManagerService roleManagerService;

    @PostMapping("/findRoles")
    public Result findRoles(@RequestBody SearchForm searchForm){
        Result result = null;
        try {
            PageInfo<RoleVO> rolesByPage = roleManagerService.findRolesByPage(searchForm);
            result = Result.success(rolesByPage);
            log.info("findRoles success info, result = {}",rolesByPage);
        } catch (Exception e) {
            result = Result.error("1",e.getLocalizedMessage());
            log.info("findRoles warn",e);
        }
        return result;
    }

    @PostMapping("/deleteRole")
        public Result deleteRole(@RequestBody @Validated IdListDto idList){
            Result result = null;
            try {
                roleManagerService.deleteRoleBatch(idList.getIds());
                result = Result.success();
                log.info("deleteRole success info, result = {}",idList);
            } catch (Exception e) {
                result = Result.error("1",e.getLocalizedMessage());
                log.info("deleteRole warn",e);
            }
            return result;
    }

    @PostMapping("/insertRole")
    public Result insertRole(@RequestBody @Validated IdListDto idList){
        Result result = null;
        try {
            roleManagerService.deleteRoleBatch(idList.getIds());
            result = Result.success();
            log.info("insertRole success info, result = {}",idList);
        } catch (Exception e) {
            result = Result.error("1",e.getLocalizedMessage());
            log.info("insertRole warn",e);
        }
        return result;
    }
}
