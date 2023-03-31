package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.common.SearchForm;
import com.example.demo.common.annotation.LoginUser;
import com.example.demo.entity.Audit;
import com.example.demo.entity.Event;
import com.example.demo.service.ArrangeService;
import com.example.demo.vo.ArrangeVO;
import com.example.demo.vo.UserVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@RestController
@RequestMapping("/arrange")
@CrossOrigin
@Slf4j
public class ArrangeController {

    @Resource
    private ArrangeService arrangeService;

    @PostMapping(value = "queryArrangeByEventId")
    public Result<?> queryArrangeByEventId(@RequestBody SearchForm searchForm){
        Result result = new Result();
        try {
            PageInfo<ArrangeVO> arrangeVOPageInfo = arrangeService.queryArrangeByEventId(searchForm);
            result= Result.success(arrangeVOPageInfo);
            log.info("success {}","查询成功");
        }catch (Exception e){
            log.info("error {}",e.getLocalizedMessage());
            result = Result.error("1","审核失败");
        }
        return result;
    }
}
