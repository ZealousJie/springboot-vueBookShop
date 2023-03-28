package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 赛事实体类
 * @ author zealousJie
 * @ version 1.0
 */
@TableName("match_event")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId
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
    @TableField(value = "event_onlineType")
    private Integer eventOnlineType;
    /**
     * 团队赛还是个人赛 1：团队 0：个人
     */
    @TableField(value = "event_teamOrPersonal")
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
    private String eventEntrants;
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
     * 赛事状态 1:进行中 0：未开始 2：已结束
     */
    private Integer eventState;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "event_createTime")
    private Date eventCreateTime;

    private String parentId;

    private Integer auditState;

    @TableField(value = "teamNumber")
    private Integer teamNumber;
}
