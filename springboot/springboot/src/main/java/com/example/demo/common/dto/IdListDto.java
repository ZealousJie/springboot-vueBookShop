package com.example.demo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdListDto implements Serializable {

    private static final long serialVersionUID = 1L;


    @Size(min = 1,message = "请选择需要删除的数据")
    private List<String> ids;

}
