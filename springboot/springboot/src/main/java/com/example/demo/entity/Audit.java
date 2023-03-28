package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@TableName("match_audit")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Audit {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String eventId;
    private String rejectMsg;
    private String auditPerson;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date auditTime;
}
