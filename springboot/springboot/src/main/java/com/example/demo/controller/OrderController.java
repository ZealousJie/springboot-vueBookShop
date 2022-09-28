package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.Orders;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.OrdersMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin
@Transactional
public class OrderController {

    @Resource
    OrdersMapper ordersMapper;
    @Resource
    BookMapper bookMapper;


    @GetMapping
    public List<Orders> getOrders() {
        return ordersMapper.selectList(null);
    }
    //@RequestBody 将json 数据转换为Java对象
    @PostMapping //下单
    public Result<?> addOrder(@RequestBody Orders orders){
        Book book = bookMapper.selectById(orders.getGoodsId());
        Integer stock = book.getStock();
        String num = orders.getNum();
        int i = Integer.parseInt(num);
        stock = stock-i;
        if (stock<0){
            return Result.error("1","库存不够");
        }
        Date date = new Date();
        orders.setCreateTime(date);
        orders.setName("购买" + book.getBookName() + "订单");
        orders.setOrderId(new SimpleDateFormat("yyyyMMdd").format(date) + System.currentTimeMillis());
        orders.setState("未支付");
        orders.setTotal(book.getPrice().multiply(BigDecimal.valueOf(i)));

        ordersMapper.insert(orders);
        book.setStock(stock);
        bookMapper.updateById(book);
        return Result.success();
    }

    @RequestMapping(value = "/deleteOrder/{id}",method = RequestMethod.GET)
    public Result<?> cancelOrder(@PathVariable("id") Integer id){
        Orders orders = ordersMapper.selectById(id);
        String num = orders.getNum();
        Integer goodsId = orders.getGoodsId();
        int i = Integer.parseInt(num);
        Book book = bookMapper.selectById(goodsId);
        Integer stock = book.getStock();
        stock =stock + i;
        book.setStock(stock);
        bookMapper.updateById(book);
        ordersMapper.deleteById(id);
        return Result.success();
    }




}
