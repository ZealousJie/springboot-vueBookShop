package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public enum EventProjectEnum {

    YXLM("英雄联盟", "1-1"),
    CSGO("CSGO", "1-1"),
    APEX("APEX", "1-1"),
    LSCS("炉石传说", "0-1"),
    GRPP("单人乒乓", "0-0"),
    BASKET("篮球", "1-0"),
    SOCCER("足球", "1-0");





    private final String name;
    private final String code;


    public static EventProjectEnum match(String key) {

        EventProjectEnum result = null;

        for (EventProjectEnum s : values()) {
            if (s.getName().equals(key)) {
                result = s;
                break;
            }
        }

        return result;
    }

    public static EventProjectEnum catchMessage(String msg) {

        EventProjectEnum result = null;

        for (EventProjectEnum s : values()) {
            if (s.getCode().equals(msg)) {
                result = s;
                break;
            }
        }

        return result;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    EventProjectEnum(String name,String code){
        this.code = code;
        this.name = name;
    }
}
