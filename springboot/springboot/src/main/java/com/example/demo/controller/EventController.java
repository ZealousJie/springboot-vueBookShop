package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.common.dto.EventDto;
import com.example.demo.entity.Book;
import com.example.demo.entity.Event;
import com.example.demo.mapper.BookMapper;
import com.example.demo.service.BookService;
import com.example.demo.service.EventService;
import com.example.demo.vo.EventVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/event")
@CrossOrigin
@Slf4j
public class EventController {

    @Resource
    private EventService eventService;
    @PostMapping("/queryAllEvents")
    public Result queryAllEvents(@RequestBody EventDto eventDto){
        Result result;
        try {
            List<EventVO> eventVOS = eventService.queryEvent(eventDto);
            result= Result.success(eventVOS);
        }catch (Exception e){
            e.printStackTrace();
            result = Result.error("0","查询失败");
        }


        return result;
    }

}
