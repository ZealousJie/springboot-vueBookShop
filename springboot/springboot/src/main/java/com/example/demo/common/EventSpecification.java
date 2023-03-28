package com.example.demo.common;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public enum EventSpecification {

    XTD("111", "线上团队电子竞技"),
    XTT("110", "线上团队体育竞技"),
    XGT("101", "线上个人体育竞技"),
    XGD("100", "线上个人电子竞技"),
    STD("011", "线下团队电子竞技"),
    STT("010", "线下团队体育竞技"),
    SGT("001", "线下个人体育竞技"),
    SGD("000", "线下个人电子竞技"),




    ;

    private final String code;
    private final String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    EventSpecification(String code,String message){
        this.code = code;
        this.message = message;
    }

}
