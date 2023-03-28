package com.example.demo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.EventProjectEnum;
import com.example.demo.common.EventSpecification;
import com.example.demo.common.dto.AttentionForm;
import com.example.demo.common.dto.EventDto;
import com.example.demo.entity.*;
import com.example.demo.mapper.*;
import com.example.demo.service.EventService;
import com.example.demo.utils.BeanCopyUtil;
import com.example.demo.utils.JsonUtil;
import com.example.demo.vo.EventVO;
import com.example.demo.vo.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Service
@Transactional
public class EventServiceImpl implements EventService {

    @Resource
    private AttentionMapper attentionMapper;
    @Resource
    private EventMapper eventMapper;
    @Resource
    private EventAttributeMapper eventAttributeMapper;
    @Resource
    private AuditMapper auditMapper;

    @Resource
    private TeamMapper teamMapper;
    @Override
    public Set<HashMap<String,String>> queryAllProjectName() {
        List<Event> events = eventMapper.queryEvent(new EventDto());
        Set<HashMap<String,String>> projectList = new HashSet<>();
        for (Event event : events) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("projectName",event.getEventProject());
            projectList.add(hashMap);
        }
        return projectList;
    }

    @Override
    public List<HashMap<String, String>> queryAllOrganizerName() {
        List<Event> events = eventMapper.queryEvent(new EventDto());
        List<HashMap<String,String>> organizerList = new ArrayList<>();
        for (Event event : events) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("organizerName",event.getOrganizer());
            organizerList.add(hashMap);
        }
        return organizerList;
    }

    @Override
    public PageInfo<EventVO> queryEvent(EventDto query, UserVO user) {
        if (query.getPageNum() != null && query.getPageSize() != null){
            PageHelper.startPage(query.getPageNum(),query.getPageSize());
        }
        List<Event> events = eventMapper.queryEvent(query);
        List<EventVO> eventVOS = buildEventVO(events,user);
        PageInfo pageInfo = new PageInfo<>(events);
        pageInfo.setList(eventVOS);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void addEvent(EventDto eventDto) {
        Event event = BeanCopyUtil.copyProperties(eventDto, Event.class);
        String eventProject = eventDto.getEventProject();
        List<Integer> projectToCode = projectToCode(eventProject);
        event.setEventType(projectToCode.get(1));
        event.setEventTeamOrPersonal(projectToCode.get(0));
        String uuid = StrUtil.uuid().replace("-", "");
        event.setId(uuid);
        event.setEventCreateTime(new Date());
        event.setAuditState(0);
        Audit audit = Audit.builder()
                .eventId(uuid).build();
        auditMapper.insert(audit);
//        event.setAuditState(1);
        String start = DateFormat.getDateTimeInstance().format(eventDto.getEventDuration().get(0));
        String end = DateFormat.getDateTimeInstance().format(eventDto.getEventDuration().get(1));
        eventAttributeMapper.insert(EventAttribute.builder().eventId(uuid).attributeName("赛事举办期间")
                .attributeValue(start+"---"+end).code(2).build());
        eventMapper.insertEvent(event);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void updateEvent(EventDto eventDto) {
        Event event = BeanCopyUtil.copyProperties(eventDto, Event.class);
        String eventProject = eventDto.getEventProject();
        List<Integer> projectToCode = projectToCode(eventProject);
        event.setEventType(projectToCode.get(1));
        event.setEventTeamOrPersonal(projectToCode.get(0));
        eventMapper.updateById(event);
    }

    @Override
    public void attention(AttentionForm attentionForm) {
        if (attentionForm.getTypeId() == 1){
            //关注
            attentionMapper.setAttention(attentionForm);
        }else {
            //取关
            attentionMapper.deleteAttention(attentionForm);
        }
    }

    @Override
    public List<Event> queryAttentionByUid(String uid) {
        return eventMapper.queryAttentionByUid(uid);
    }

    @Override
    public EventVO queryEventById(String projectId,UserVO userVO) {
        Event event = eventMapper.selectById(projectId);
        List<Event> events =new ArrayList<>(4);
        events.add(event);
        List<EventVO> eventVOS = buildEventVO(events, userVO);
        return eventVOS.get(0);
    }


    public List<EventVO> buildEventVO(List<Event> list,UserVO user){
        QueryWrapper<EventAttribute> attributeQueryWrapper = new QueryWrapper<>();
        List<EventAttribute> eventAttributes = eventAttributeMapper.selectList(attributeQueryWrapper);

        List<String> attention = attentionMapper.getAttention(AttentionForm.builder().uid(user.getUid()).build());
        ArrayList<EventVO> eventVOS = new ArrayList<>();
        for (Event event : list) {
            List<Team> teams = null;
            String eventEntrants = event.getEventEntrants();
            if (StrUtil.isNotBlank(eventEntrants)){
                List<Integer> teamIds = JsonUtil.fromJsonList(eventEntrants);
                teams = teamMapper.queryByIds(teamIds);
            }
            List<String> collect = null;
            String attentionState = "NOT_ATTENTION";
            if (!attention.isEmpty()){
                collect = attention.stream().filter(x -> x.equals(event.getId())).collect(Collectors.toList());
            }
            if (collect != null && collect.size() > 0){
                attentionState = "IS_ATTENTION";
            }

            List<EventAttribute> attributes = eventAttributes.stream().filter(e -> e.getEventId().equals(event.getId())).sorted(Comparator.comparing(EventAttribute::getCode)).collect(Collectors.toList());
            EventVO eventVO = EventVO.builder()
                    .attributeList(attributes)
                    .eventAttentionState(attentionState)
                    .eventEntrants(teams)
                    .eventName(event.getEventName())
                    .eventProject(event.getEventProject())
                    .bonus(event.getBonus()!= null? event.getBonus() :0)
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
                    .eventSpecification
                            (confirmEventSpecification(event.getEventOnlineType(),event.getEventTeamOrPersonal(),event.getEventType()))
                    .build();
            eventVOS.add(eventVO);
        }
        return eventVOS;
    }


    /**
     * 确认赛事规格
     * @param online 线上线下
     * @param type  体育电竞
     * @param team  团体个人
     * @return 赛事规格
     */
    public String confirmEventSpecification(Integer online,Integer type,Integer team){
        String code = ""+online+type+team;
        String result;
        switch (code){
            case "111":
                result = EventSpecification.XTD.getMessage();
                break;
            case "110":
                result = EventSpecification.XTT.getMessage();
                break;
            case "101":
                result = EventSpecification.XGD.getMessage();
                break;
            case "100":
                result = EventSpecification.XGT.getMessage();
                break;
            case "011":
                result = EventSpecification.STD.getMessage();
                break;
            case "010":
                result = EventSpecification.STT.getMessage();
                break;
            case "001":
                result =  EventSpecification.SGD.getMessage();
                break;
            case "000":
                result = EventSpecification.SGT.getMessage();
                break;
            case "200":
                result = "全规格个人体育竞技";
                break;
            case "210":
                result = "全规格团队体育竞技";
                break;
            case "201":
                result = "全规格个人电子竞技";
                break;
            case "211":
                result = "全规格团队电子竞技";
                break;
            default:
                result = "赛事规格异常";
        }

        return result;
    }

    public List<Integer> projectToCode(String projectName){
        ArrayList<Integer> code = new ArrayList<>();
        String targetCode = EventProjectEnum.match(projectName).getCode();
        String[] split = targetCode.split("-");
        code.add(Integer.parseInt(split[0]));
        code.add(Integer.parseInt(split[1]));
        return code;
    }
}
