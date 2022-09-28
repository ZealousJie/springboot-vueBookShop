package com.example.demo.controller;

import lombok.Data;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Data
public class AliPay {
    private String traceNo;
    private double totalAmount;
    private String subject;
    private String alipayTraceNo;
}
