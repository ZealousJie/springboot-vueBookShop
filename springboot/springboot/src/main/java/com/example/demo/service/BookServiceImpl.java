package com.example.demo.service;

import com.example.demo.entity.Audit;
import com.example.demo.entity.Book;
import com.example.demo.mapper.AuditMapper;
import com.example.demo.mapper.BookMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Service
public class BookServiceImpl implements BookService{
    @Resource
    private AuditMapper auditMapper;
    @Resource
    private BookMapper bookMapper;
    @Override
    public int addToAudit(Book book) {
        Audit audit = new Audit();
        audit.setAuthor(book.getAuthor());
        audit.setBookName(book.getBookName());
        audit.setPricing(book.getPrice());
        audit.setOverrule_reason("暂无");
        audit.setState("待审核");
        audit.setPublishTime(book.getCreateTime());
        bookMapper.insert(book);
        return auditMapper.insert(audit);
    }
}
