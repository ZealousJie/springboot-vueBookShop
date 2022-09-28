package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("book")
@Data
public class Book {
    //设置主键id 自增
    @TableId(type = IdType.AUTO)
    private Integer bid;
    private String bookName;
    private BigDecimal price;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;//出版时间
    private String author;
    private String cover;
    private Integer stock;//库存
}
