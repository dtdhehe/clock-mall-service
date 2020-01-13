package com.example.clockmallservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.clockmallservice.entity.Orders;
import com.example.clockmallservice.mapper.OrdersMapper;
import com.example.clockmallservice.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 16:36
 * @description
 **/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper,Orders> implements OrdersService {
}
