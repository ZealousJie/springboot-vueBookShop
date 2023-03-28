package com.example.demo.controller;

import com.example.demo.common.dto.EventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@RestController
@RequestMapping("/eventExport")
@CrossOrigin
@Slf4j
public class EventExportController {


    @GetMapping(value = "exportXml")
    public void exportXml(HttpServletResponse httpServletResponse, @RequestBody EventDto eventDto){

    }
}
