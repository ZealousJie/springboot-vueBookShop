package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.mapper.AuditMapper;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.BookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/book")
@CrossOrigin
public class BookController {

    @Resource
    BookMapper bookMapper;
    @Resource
    BookService bookService;


    //@RequestBody 将json 数据转换为Java对象
    @PostMapping //上架请求 加入到审核列表
    public Result<?> save(@RequestBody Book book){
        book.setState("待审核");
        bookService.addToAudit(book);

        return Result.success();
    }



    @GetMapping//查询
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        //new一个mp提供的进行分页的Page对象
        Page<Book> page = new Page<>(pageNum, pageSize);
        //用Wrappers 生成一个用来完成模糊查询的 wrapper
        LambdaQueryWrapper<Book> wrapper = Wrappers.<Book>lambdaQuery();
        if(StrUtil.isNotBlank(search)){//当search 不为空就进行模糊查询 hutool提供的方法 帮助判断非空？
            wrapper.like(Book::getBookName, search);
        }
        //用mp的selectPage方法完成分页
        Page<Book> bookPage = bookMapper.selectPage(page, wrapper);
        return Result.success(bookPage);
    }
    @PutMapping //更新
    public Result<?> update(@RequestBody Book book){
        bookMapper.updateById(book);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> deleteBook(@PathVariable Integer bid){
        bookMapper.deleteById(bid);
        return Result.success();
    }
}
