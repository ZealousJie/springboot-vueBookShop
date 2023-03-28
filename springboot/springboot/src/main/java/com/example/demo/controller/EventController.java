package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.example.demo.common.Result;
import com.example.demo.common.annotation.LoginUser;
import com.example.demo.common.dto.AttentionForm;
import com.example.demo.common.dto.EventDto;
import com.example.demo.common.dto.IdListDto;
import com.example.demo.entity.Event;
import com.example.demo.entity.User;
import com.example.demo.mapper.AuditMapper;
import com.example.demo.mapper.EventMapper;
import com.example.demo.service.EventService;
import com.example.demo.vo.EventVO;
import com.example.demo.vo.UserVO;
import com.github.pagehelper.PageInfo;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/event")
@CrossOrigin
@Slf4j
public class EventController {

    @Resource
    private EventService eventService;
    @Resource
    private EventMapper eventMapper;

    @PostMapping("/queryAllEvents")
    public Result<?> queryAllEvents(@LoginUser UserVO user,@RequestBody EventDto eventDto){
        Result<?> result;
        try {
            PageInfo<EventVO> queryResult = eventService.queryEvent(eventDto,user);
            result= Result.success(queryResult);
        }catch (Exception e){
            e.printStackTrace();
            result = Result.error("0","查询失败");
        }

        return result;
    }


    /**
     * 拿到所有项目名
     */
    @PostMapping("/queryAllProjectName")
    public Result<?> queryAllProjectName(){
        Result<?> result;
        try {
            Set<HashMap<String,String>> queryAllProjectName = eventService.queryAllProjectName();
            result= Result.success(queryAllProjectName);
        }catch (Exception e){
            e.printStackTrace();
            result = Result.error("0","查询失败");
        }

        return result;
    }



    @PostMapping("/queryProjectNum")
    public Result<?> queryProjectNum(){
        Result<?> result;
        try {
            List<Map<String,String>>  projectNum = eventMapper.queryProjectNum();
            result= Result.success(projectNum);
        }catch (Exception e){
            e.printStackTrace();
            result = Result.error("0","查询失败");
        }

        return result;
    }

    @PostMapping("/queryAllOrganizerName")
    public Result<?> queryAllOrganizerName(){
        Result<?> result;
        try {
            List<HashMap<String,String>> queryAllOrganizerName = eventService.queryAllOrganizerName();
            result= Result.success(queryAllOrganizerName);
        }catch (Exception e){
            e.printStackTrace();
            result = Result.error("0","查询失败");
        }

        return result;
    }

    @PostMapping("/addEvent")
    public Result<?> addEvent(@RequestBody EventDto eventDto){
        Result<?> result;
        try {
            eventService.addEvent(eventDto);
            result= Result.success();
        }catch (Exception e){
            e.printStackTrace();
            result = Result.error("0","新增失败");
        }

        return result;
    }

    @PostMapping("/updateEvent")
    public Result<?> updateEvent(@RequestBody EventDto eventDto){
        Result<?> result;
        try {
            eventService.updateEvent(eventDto);
            result= Result.success();
        }catch (Exception e){
            e.printStackTrace();
            result = Result.error("0","新增失败");
        }

        return result;
    }

    @PostMapping("attentionOrNot")
    public Result<?> attentionOrNot(@RequestBody AttentionForm attentionForm,@LoginUser UserVO user){
        if (StrUtil.isNotBlank(user.getMsg())){
            return Result.error("1",user.getMsg());
        }
        log.info("attention info {}",attentionForm);
        if (StrUtil.isBlank(attentionForm.getEventId()) || StrUtil.isBlank(user.getUid())){
            return Result.error("1","请求参数不能为空");
        }
        attentionForm.setUid(user.getUid());
        Result<?> result;
        try {
            eventService.attention(attentionForm);
            result= Result.success();
        }catch (Exception e){
            e.printStackTrace();
            result = Result.error("1","切换失败");
        }

        return result;
    }
    @PostMapping("/deleteEvent")
    public Result<?> deleteEvent(@RequestBody @Validated IdListDto idList){
        Result<?> result;
        try {
            eventMapper.deleteBatchIds(idList.getIds());
            result= Result.success();
        }catch (Exception e){
            e.printStackTrace();
            result = Result.error("0","删除失败");
        }

        return result;
    }




}
