package com.example.demo.vo;

import com.example.demo.entity.Team;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArrangeVO {


    private Integer id;
    private Team teamOne;
    private Team teamTwo;
    private Integer victoryTeam;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date matchTime;
}
