package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.common.annotation.LoginUser;
import com.example.demo.entity.Audit;
import com.example.demo.entity.Event;
import com.example.demo.mapper.AuditMapper;
import com.example.demo.mapper.EventMapper;
import com.example.demo.service.AuditService;
import com.example.demo.vo.AuditVO;
import com.example.demo.vo.UserVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@RestController
@RequestMapping("/audit")
@CrossOrigin
@Slf4j
public class AuditController {
    @Resource
    private AuditService auditService;

    @Resource
    private AuditMapper auditMapper;
    @Resource
    private EventMapper eventMapper;

    @GetMapping(value = "findAllAudit")//查询
    public Result<?> findAllAudit(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        Result<?> result;
        try {
            PageInfo<AuditVO> queryResult = auditService.queryAudit(pageNum,pageSize,search);
            result= Result.success(queryResult);
        }catch (Exception e){
            e.printStackTrace();
            result = Result.error("1","查询失败");
        }

        return result;
    }
    //审核通过根据id更新状态
    @GetMapping(value = "/updateAuditDo/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> updateState(@PathVariable("id") Integer id, @LoginUser UserVO userVO){
        Result result = new Result();
        try {
            Audit audit = auditMapper.selectById(id);
            audit.setAuditPerson(userVO.getRealName());
            audit.setAuditTime(new Date());
            auditMapper.updateById(audit);
            Event event = eventMapper.selectById(audit.getEventId());
            event.setAuditState(1);
            eventMapper.updateById(event);
            result = Result.success();
            log.info("success {}","审核成功");
        }catch (Exception e){
            log.info("error {}",e.getLocalizedMessage());
            result = Result.error("1","审核失败");
        }
        return result;
    }

    //审核不通过 更新状态
    @PostMapping(value = "/updateAuditDont")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> updateReason(@RequestBody Audit audit,@LoginUser UserVO userVO){
        Result result = new Result();
        try {
            audit.setAuditPerson(userVO.getRealName());
            audit.setAuditTime(new Date());
            auditMapper.updateById(audit);
            Event event = eventMapper.selectById(audit.getEventId());
            event.setAuditState(2);
            eventMapper.updateById(event);
            result = Result.success();
            log.info("success {}","审核成功");
        }catch (Exception e){
            log.info("error {}",e.getLocalizedMessage());
            result = Result.error("1","审核失败");
        }
        return result;
    }
}
