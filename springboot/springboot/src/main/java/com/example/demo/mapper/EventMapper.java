package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.common.dto.EventDto;
import com.example.demo.entity.Event;

import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface EventMapper extends BaseMapper<Event> {

    List<Event> queryEvent(EventDto eventDto);
}
