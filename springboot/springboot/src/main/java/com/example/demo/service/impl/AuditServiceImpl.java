package com.example.demo.service.impl;

import com.example.demo.mapper.AuditMapper;
import com.example.demo.service.AuditService;
import com.example.demo.vo.AuditVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Service
public class AuditServiceImpl implements AuditService {
    @Resource
    private AuditMapper auditMapper;

    @Override
    public PageInfo<AuditVO> queryAudit(Integer pageNum, Integer pageSize, String search) {
        if (pageNum != null && pageSize != null){
            PageHelper.startPage(pageNum,pageSize);
        }
        List<AuditVO> allAudit = auditMapper.getAllAudit(search);
        PageInfo pageInfo = new PageInfo<>();
        pageInfo.setList(allAudit);
        return pageInfo;
    }
}
