package com.example.demo.vo;

import com.example.demo.entity.EventAttribute;
import com.example.demo.entity.Team;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EventVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 赛事名 如 ：湖南理工月莲杯
     */
    private String eventName;
    /**
     * 赛事项目 Lol 足球
     */
    private String eventProject;
    /**
     * 是否是线上比赛 1：线上 0：线下
     */
    private Integer eventOnlineType;
    /**
     * 团队赛还是个人赛 1：团队 0：个人
     */
    private Integer eventTeamOrPersonal;
    /**
     * 赛事类型 1：电竞 0：体育
     */
    private Integer eventType;
    /**
     * 赛事阶段 小组赛 半决赛 决赛等
     */
    private String eventStage;
    /**
     * 比赛队伍（团队赛专属 json字符串可转 List）
     */
    private List<Team> eventEntrants;
    /**
     * 赛事地点
     */
    private String eventVenue;
    /**
     * 单人票价
     */
    private Integer unitPrice;
    /**
     * 剩余票数
     */
    private Integer remainingTickets;
    /**
     * 赛事奖金
     */
    private Integer bonus;
    /**
     * 举办方代表人id 或组织人id
     */
    private String organizer;
    /**
     * 赛事关注状态 eventState 关注IS_ATTENTION  非关注NOT_ATTENTION
     */
    private String eventAttentionState;
    /**
     * 赛事状态 1:进行中 0：未开始 2：已结束
     */
    private Integer eventState;

    private List<EventAttribute> attributeList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date eventCreateTime;

    private String  eventSpecification;
}
