package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@TableName("match_attribute")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventAttribute {

    private String id;
    private String eventId;
    private String attributeName;
    private Date attributeTime;
    private String attributeValue;
    private String code;
}
