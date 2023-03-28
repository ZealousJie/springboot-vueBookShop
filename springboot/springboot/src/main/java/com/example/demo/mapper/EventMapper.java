package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.common.dto.EventDto;
import com.example.demo.entity.Event;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface EventMapper extends BaseMapper<Event> {

    List<Event> queryEvent(EventDto eventDto);

    void insertEvent(Event event);

    List<Event> queryAttentionByUid(@Param("uid") String uid);


    @MapKey("event_project")
    List<Map<String,String>> queryProjectNum();



}
