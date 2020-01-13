package com.example.clockmallservice.controller;

import com.example.clockmallservice.service.OrdersInfoService;
import com.example.clockmallservice.service.OrdersService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 16:40
 * @description
 **/
@Api(tags = "订单")
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrdersInfoService infoService;

}
