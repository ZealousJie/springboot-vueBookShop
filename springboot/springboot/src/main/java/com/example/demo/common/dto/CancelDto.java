package com.example.demo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CancelDto {

    private String orderId;

    private String eventId;
}
