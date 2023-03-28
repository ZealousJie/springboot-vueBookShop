package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.common.dto.OrderDto;
import com.example.demo.entity.Event;
import com.example.demo.entity.Orders;
import com.example.demo.mapper.EventMapper;
import com.example.demo.mapper.OrdersMapper;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
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
@Transactional(rollbackFor = Exception.class)
public class OrderController {

    @Resource
    OrdersMapper ordersMapper;
    @Resource
    EventMapper eventMapper;


    @GetMapping("queryOrder")
    public Result<?> getOrders() {
        Result result = null;
        try {
            List<Orders> orders = ordersMapper.selectList(null);
            result = Result.success(orders);
        }catch (Exception e){
            result = Result.error("1","查询失败");
        }
        return result;
    }
    //@RequestBody 将json 数据转换为Java对象
    @PostMapping ("addOrder")
    public Result<?> addOrder(@RequestBody OrderDto orderDto){
        Event event = eventMapper.selectById(orderDto.getEventId());
        int stock = event.getRemainingTickets();
        int num = orderDto.getNumber();
        stock = stock-num;
        if (stock<0){
            return Result.error("1","已无余票");
        }
        Date date = new Date();
        Orders order = Orders.builder()
                .createTime(date)
                .orderId(new SimpleDateFormat("yyyyMMdd").format(date) + System.currentTimeMillis())
                .name("购买" + event.getEventName() + "订单")
                .state(1)
                .orderType(1)
                .total(orderDto.getPrice()).build();

        ordersMapper.insert(order);
        event.setRemainingTickets(stock);
        eventMapper.updateById(event);
        return Result.success();
    }

    @RequestMapping(value = "/deleteOrder/{id}",method = RequestMethod.GET)
    public Result<?> cancelOrder(@PathVariable("id") Integer id){
//        Orders orders = ordersMapper.selectById(id);
//        String num = orders.getNum();
//        Integer goodsId = orders.getGoodsId();
//        int i = Integer.parseInt(num);
//        Book book = bookMapper.selectById(goodsId);
//        Integer stock = book.getStock();
//        stock =stock + i;
//        book.setStock(stock);
//        bookMapper.updateById(book);
//        ordersMapper.deleteById(id);
        return Result.success();
    }




}
