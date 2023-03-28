package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@Data
public class EventAttribute {

    private String eventId;
    private String attributeName;
    private String attributeValue;
    private Integer code;
}
