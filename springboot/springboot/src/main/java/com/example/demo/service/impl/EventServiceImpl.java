package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.dto.EventDto;
import com.example.demo.entity.Event;
import com.example.demo.entity.EventAttribute;
import com.example.demo.mapper.EventAttributeMapper;
import com.example.demo.mapper.EventMapper;
import com.example.demo.service.EventService;
import com.example.demo.vo.EventVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Service
public class EventServiceImpl implements EventService {
    @Resource
    private EventMapper eventMapper;
    @Resource
    private EventAttributeMapper eventAttributeMapper;

    @Override
    public List<EventVO> queryEvent(EventDto query) {

        return buildEventVO(eventMapper.queryEvent(query));
    }


    public List<EventVO> buildEventVO(List<Event> list){
        QueryWrapper<EventAttribute> attributeQueryWrapper = new QueryWrapper<>();
        List<EventAttribute> eventAttributes = eventAttributeMapper.selectList(attributeQueryWrapper);
        ArrayList<EventVO> eventVOS = new ArrayList<>();
        for (Event event : list) {
            List<EventAttribute> attributes = eventAttributes.stream().filter(e -> e.getEventId().equals(event.getId())).collect(Collectors.toList());
            EventVO eventVO = EventVO.builder()
                    .attributeList(attributes)
                    .eventEntrants(event.getEventEntrants())
                    .eventName(event.getEventName())
                    .eventProject(event.getEventProject())
                    .bonus(event.getBonus())
                    .eventOnlineType(event.getEventOnlineType())
                    .eventStage(event.getEventStage())
                    .eventState(event.getEventState())
                    .eventType(event.getEventType())
                    .eventVenue(event.getEventVenue())
                    .eventTeamOrPersonal(event.getEventTeamOrPersonal())
                    .id(event.getId())
                    .organizer(event.getOrganizer())
                    .remainingTickets(event.getRemainingTickets())
                    .unitPrice(event.getUnitPrice())
                    .eventCreateTime(event.getEventCreateTime())
                    .build();
            eventVOS.add(eventVO);
        }
        return eventVOS;
    }
}
