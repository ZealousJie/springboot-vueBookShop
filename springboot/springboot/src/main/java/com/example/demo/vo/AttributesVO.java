package com.example.demo.vo;

import com.example.demo.entity.EventAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributesVO {

    private String eventId;

    private List<EventAttribute> eventAttributeList;
}
