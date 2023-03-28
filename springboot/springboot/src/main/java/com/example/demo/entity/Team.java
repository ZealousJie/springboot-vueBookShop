package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@TableName("match_team")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Integer teamId;
    private String teamName;
    private String teamMember;
    private String eventId;
}
