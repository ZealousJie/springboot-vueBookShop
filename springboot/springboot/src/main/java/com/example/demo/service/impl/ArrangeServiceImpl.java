package com.example.demo.service.impl;

import com.example.demo.common.SearchForm;
import com.example.demo.entity.Arrange;
import com.example.demo.entity.Team;
import com.example.demo.mapper.ArrangeMapper;
import com.example.demo.mapper.AuditMapper;
import com.example.demo.mapper.TeamMapper;
import com.example.demo.service.ArrangeService;
import com.example.demo.service.AuditService;
import com.example.demo.vo.ArrangeVO;
import com.example.demo.vo.AuditVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Service
public class ArrangeServiceImpl implements ArrangeService {

    @Resource
    private ArrangeMapper arrangeMapper;
    @Resource
    private TeamMapper teamMapper;

    @Override
    public PageInfo<ArrangeVO> queryArrangeByEventId(SearchForm searchForm) {
        PageHelper.startPage(searchForm.getPage(),searchForm.getRows());
        List<Arrange> arranges = arrangeMapper.queryArrangeByEventId(searchForm.getEventId());
        List<ArrangeVO> arrangeVOS = new ArrayList<>();
        for (Arrange arrange : arranges) {
            Team teamOne = teamMapper.selectById(arrange.getTeam_idOne());
            Team teamTwo = teamMapper.selectById(arrange.getTeam_idTwo());
            ArrangeVO arrangeVO = ArrangeVO.builder().teamOne(teamOne).teamTwo(teamTwo).victoryTeam(arrange.getVictoryTeam()).matchTime(arrange.getMatchTime()).id(arrange.getId()).build();
            arrangeVOS.add(arrangeVO);
        }

        PageInfo pageInfo = new PageInfo<>();
        pageInfo.setList(arrangeVOS);
        return pageInfo;
    }
}
