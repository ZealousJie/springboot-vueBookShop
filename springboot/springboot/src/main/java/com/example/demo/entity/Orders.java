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

@TableName("match_order")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Orders {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String orderId;
    private Integer orderType;
    private String alipayNo;
    private BigDecimal total;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;
    private Integer state;
    //    //订单到期时间；存储时间戳
//    private long dueDate;

}


