package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.common.annotation.LoginUser;
import com.example.demo.common.dto.NewEmailDto;
import com.example.demo.entity.EventAttribute;
import com.example.demo.entity.News;
import com.example.demo.mapper.EventAttributeMapper;
import com.example.demo.mapper.NewsMapper;
import com.example.demo.service.NewService;
import com.example.demo.vo.AttributesVO;
import com.example.demo.vo.EventVO;
import com.example.demo.vo.UserVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/news")
@CrossOrigin
public class NewsController {

    @Resource
    NewsMapper newsMapper;
    @Resource
    private NewService newService;

    @Resource
    private EventAttributeMapper eventAttributeMapper;

    @PostMapping(value = "sendMail")
    public Result<?> sendMail(NewEmailDto news, @LoginUser UserVO userVO) throws MessagingException {
        Result<?> result;
        newService.sendMail(news,userVO);
        result= Result.success();
        return result;
    }

    @PutMapping
    public Result<?> update(@RequestBody News news) {
        newsMapper.updateById(news);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> update(@PathVariable Long id) {
        newsMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(newsMapper.selectById(id));
    }

    @PostMapping("/addAttribute")
    public Result<?> addAttribute(@RequestBody AttributesVO attributesVO) {
        return Result.success();
    }

    @PostMapping("/getAttribute")
    public Result<?> getAttribute(@RequestParam("eventId") String eventId) {
        QueryWrapper<EventAttribute> eventAttributeQueryWrapper = new QueryWrapper<>();
        eventAttributeQueryWrapper.lambda().eq(EventAttribute::getEventId,eventId);
        List<EventAttribute> eventAttributes = eventAttributeMapper.selectList(eventAttributeQueryWrapper);
        return Result.success(eventAttributes);
    }

}