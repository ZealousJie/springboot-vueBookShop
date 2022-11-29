package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@TableName("audit")
@Data
public class Audit {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer bid;
    private String bookName;
    private String author;
    private String state;
    private String overruleReason;
    private String auditType;
    private String auditPerson;
    private BigDecimal pricing;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date publishTime;
}
