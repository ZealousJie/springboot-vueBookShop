package com.example.demo.service;

import com.example.demo.common.dto.AttentionForm;
import com.example.demo.common.dto.EventDto;
import com.example.demo.entity.Event;
import com.example.demo.entity.User;
import com.example.demo.vo.EventVO;
import com.example.demo.vo.UserVO;
import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface EventService {

    Set<HashMap<String,String>> queryAllProjectName();
    List<HashMap<String,String>> queryAllOrganizerName();
    PageInfo<EventVO> queryEvent(EventDto query, UserVO user);
    void addEvent(EventDto eventDto);
    void updateEvent(EventDto eventDto);

    void attention(AttentionForm attentionForm);

    List<Event> queryAttentionByUid(String uid);

    EventVO queryEventById(String projectId,UserVO userVO);

}
