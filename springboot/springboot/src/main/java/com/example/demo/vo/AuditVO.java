package com.example.demo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditVO {

    private Integer id;
    private String eventId;
    private String eventName;
    /**
     * 赛事项目 Lol 足球
     */
    private String eventProject;
    private String eventVenue;
    private String auditState;
    private String rejectMsg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;
    private String auditPerson;
    private String organizer;
}
