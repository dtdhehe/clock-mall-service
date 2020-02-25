package com.example.clockmallservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.clockmallservice.entity.Orders;
import com.example.clockmallservice.vo.OrderResultVO;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 16:36
 * @description
 **/
public interface OrdersService extends IService<Orders> {

    /**
     * 查询我的订单
     * @param iPage
     * @param queryWrapper
     * @return
     */
    IPage<OrderResultVO> queryMyOrder(IPage<OrderResultVO> iPage, QueryWrapper<OrderResultVO> queryWrapper);

    /**
     * 查询全部订单
     * @param iPage
     * @param queryWrapper
     * @return
     */
    IPage<Orders> getAllOrders(IPage<Orders> iPage, QueryWrapper<Orders> queryWrapper);

    Orders getOrderInfo(String id);
}
