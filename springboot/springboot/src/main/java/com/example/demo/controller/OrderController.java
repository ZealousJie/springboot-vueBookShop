package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.common.dto.CancelDto;
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
                .name(event.getEventName() + "的门票订单")
                .state(1)
                .orderType(1)
                .eventId(orderDto.getEventId())
                .buyNum(num)
                .total(orderDto.getPrice()).build();

        ordersMapper.insert(order);
        event.setRemainingTickets(stock);
        eventMapper.updateById(event);
        return Result.success();
    }

    @PostMapping(value = "/deleteOrder")
    public Result<?> cancelOrder(@RequestBody CancelDto cancelDto){
        Orders orders = ordersMapper.selectById(cancelDto.getOrderId());
        int num = orders.getBuyNum();
        Event event = eventMapper.selectById(orders.getEventId());
        Integer stock = event.getRemainingTickets();
        stock =stock + num;
        event.setRemainingTickets(stock);
        eventMapper.updateById(event);
        ordersMapper.deleteById(cancelDto.getOrderId());
        return Result.success();
    }




}
