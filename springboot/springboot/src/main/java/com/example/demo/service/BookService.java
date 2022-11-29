package com.example.demo.service;

import com.example.demo.entity.Book;

import javax.servlet.http.HttpServletRequest;

/**
 * @ author zealousJie
 * @ version 1.0
 */
public interface BookService {
    int addToAudit(Book book);
}
