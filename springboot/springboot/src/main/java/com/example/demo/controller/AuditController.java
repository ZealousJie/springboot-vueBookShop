package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Audit;
import com.example.demo.entity.Book;
import com.example.demo.mapper.AuditMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@RestController
@RequestMapping("/audit")
@CrossOrigin
public class AuditController {
    @Resource
    private AuditMapper auditMapper;

    @GetMapping//查询
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        //new一个mp提供的进行分页的Page对象
        Page<Audit> page = new Page<>(pageNum, pageSize);
        //用Wrappers 生成一个用来完成模糊查询的 wrapper
        LambdaQueryWrapper<Audit> wrapper = Wrappers.<Audit>lambdaQuery();
        if(StrUtil.isNotBlank(search)){//当search 不为空就进行模糊查询 hutool提供的方法 帮助判断非空？
            wrapper.like(Audit::getBookName, search);
        }
        //用mp的selectPage方法完成分页
        Page<Audit> auditPage = auditMapper.selectPage(page, wrapper);
        return Result.success(auditPage);
    }
}
