package com.example.demo.service;

import com.example.demo.common.SearchForm;
import com.example.demo.vo.ArrangeVO;
import com.example.demo.vo.AuditVO;
import com.github.pagehelper.PageInfo;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface ArrangeService {


    PageInfo<ArrangeVO> queryArrangeByEventId(SearchForm searchForm);
}
