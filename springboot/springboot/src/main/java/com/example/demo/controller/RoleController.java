package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.common.SearchForm;
import com.example.demo.common.dto.IdListDto;
import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.CustomException2;
import com.example.demo.entity.Permission;
import com.example.demo.service.RoleManagerService;
import com.example.demo.vo.RoleDTO;
import com.example.demo.vo.RoleVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    public Result<?> findRoles(@RequestBody SearchForm searchForm){
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
    @PostMapping("/findRolesMsg")
    public Result<?> findRolesMsg(@RequestBody SearchForm searchForm){
        Result result = null;
        try {
            PageInfo<RoleDTO> rolesByPage = roleManagerService.findRoles(searchForm);
            result = Result.success(rolesByPage);
            log.info("findRoles success info, result = {}",rolesByPage);
        } catch (Exception e) {
            result = Result.error("1",e.getLocalizedMessage());
            log.info("findRoles warn",e);
        }
        return result;
    }

    @GetMapping("/findPermission")
    public Result<?> findPermission(){
        Result<?> result;
        try {
            List<Permission> permission = roleManagerService.findPermission();
            result = Result.success(permission);
            log.info("findRoles success info, result = {}",permission);
        } catch (Exception e) {
            result = Result.error("1",e.getLocalizedMessage());
            log.info("findRoles warn",e);
        }
        return result;
    }

    @PostMapping("/changePermission")
    public Result<?> changePermission(@RequestBody RoleVO roleVO){
        Result<?> result;
        try {
            roleManagerService.changePermission(roleVO.getRid(),roleVO.getPermissionList(),roleVO.getCurrentRoleIds());
            result = Result.success();
            log.info("success");
        }catch (CustomException e){
            result = Result.error("1",e.getLocalizedMessage());
            log.info("error",e);
        }catch (CustomException2 e){
            result = Result.error("2",e.getLocalizedMessage());
            log.info("error",e);
        }catch (Exception e){
            result = Result.error("1","系统异常");
            log.info("error",e);
        }
        return result;
    }

    @PostMapping("/deleteRole")
        public Result<?> deleteRole(@RequestBody @Validated IdListDto idList){
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
    public Result<?> insertRole(@RequestBody @Validated RoleDTO roleDTO){
        Result<?> result = null;
        try {
            roleManagerService.insertRole(roleDTO);
            result = Result.success();
            log.info("insertRole success info, result = {}",roleDTO);
        } catch (Exception e) {
            result = Result.error("1",e.getLocalizedMessage());
            log.info("insertRole warn",e);
        }
        return result;
    }
    @PostMapping("/updateRole")
    public Result<?> updateRole(@RequestBody @Validated RoleVO roleDTO){
        Result<?> result = null;
        try {
            roleManagerService.updateRole(roleDTO);
            result = Result.success();
            log.info("updateRole success info, result = {}",roleDTO);
        } catch (Exception e) {
            result = Result.error("1",e.getLocalizedMessage());
            log.info("updateRole warn",e);
        }
        return result;
    }
}
