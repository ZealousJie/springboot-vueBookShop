package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Orders {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer goodsId;
    private String name;
    private String orderId;
    private String alipayNo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date payTime;
    private String state;//这里记得改成 int 再写一个状态表 状态实体类
    private BigDecimal total;
    private String num;
//    //订单到期时间；存储时间戳
//    private long dueDate;
}


