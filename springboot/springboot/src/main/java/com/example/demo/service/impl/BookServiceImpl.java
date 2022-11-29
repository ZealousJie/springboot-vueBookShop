package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.entity.Audit;
import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.mapper.AuditMapper;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.BookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Service
public class BookServiceImpl implements BookService {
    @Resource
    private AuditMapper auditMapper;
    @Resource
    private BookMapper bookMapper;

    @Override
    public int addToAudit(Book book) {
        Audit audit = new Audit();
        audit.setAuditType("上架审核");
        audit.setBid(0);
        audit.setAuthor(book.getAuthor());
        audit.setBookName(book.getBookName());
        audit.setPricing(book.getPrice());
        audit.setOverruleReason("暂无");
        audit.setState("待审核");
        audit.setPublishTime(book.getCreateTime());
        book.setState("待审核");
        bookMapper.insert(book);
        return auditMapper.insert(audit);
    }
}
