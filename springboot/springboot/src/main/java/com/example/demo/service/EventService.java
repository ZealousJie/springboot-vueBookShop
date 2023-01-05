package com.example.demo.service;

import com.example.demo.common.dto.EventDto;
import com.example.demo.entity.Event;
import com.example.demo.vo.EventVO;

import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface EventService {

    List<EventVO> queryEvent(EventDto query);
}
