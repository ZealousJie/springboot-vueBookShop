package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("match_arrange")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Arrange {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String eventId;
    @TableField(value = "team_id_one")
    private Integer team_idOne;
    @TableField(value = "team_id_two")
    private Integer team_idTwo;
    private Integer victoryTeam;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date matchTime;
}
