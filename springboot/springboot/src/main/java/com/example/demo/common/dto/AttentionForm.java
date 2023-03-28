package com.example.demo.common.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Data
@Builder
public class AttentionForm {

    private Integer typeId;

    private String uid;

    private String eventId;
}
