package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页查询统一表单
 * @ author zealousJie
 * @ version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchForm{
    /**
     * 每一页的条数
     */
    private Integer rows;
    /**
     * 页码
     */
    private Integer page;

    /**
     * 排序字段
     */
    private String sort;
    /**
     * 排序方式
     * true为升序
     * false为降序
     */
    private boolean order;
    /**
     * 搜索文本
     */
    private String search;

    private String column;

}
