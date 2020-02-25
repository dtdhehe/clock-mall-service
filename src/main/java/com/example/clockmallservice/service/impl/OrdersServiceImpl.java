package com.example.clockmallservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.clockmallservice.entity.Orders;
import com.example.clockmallservice.mapper.OrdersMapper;
import com.example.clockmallservice.service.OrdersService;
import com.example.clockmallservice.vo.OrderResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 16:36
 * @description
 **/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper,Orders> implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public IPage<OrderResultVO> queryMyOrder(IPage<OrderResultVO> iPage, QueryWrapper<OrderResultVO> queryWrapper) {
        return ordersMapper.queryMyOrder(iPage,queryWrapper);
    }

    @Override
    public IPage<Orders> getAllOrders(IPage<Orders> iPage, QueryWrapper<Orders> queryWrapper) {
        return ordersMapper.getAllOrders(iPage,queryWrapper);
    }

    @Override
    public Orders getOrderInfo(String id) {
        return ordersMapper.getOrderInfo(id);
    }
}
