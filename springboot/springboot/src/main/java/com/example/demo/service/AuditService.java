package com.example.demo.service;

import com.example.demo.vo.AuditVO;
import com.github.pagehelper.PageInfo;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface AuditService {

    PageInfo<AuditVO>  queryAudit(Integer pageNum,Integer pageSize,String search);
}
