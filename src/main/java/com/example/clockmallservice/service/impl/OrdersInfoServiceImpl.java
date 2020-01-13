package com.example.clockmallservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.clockmallservice.entity.OrdersInfo;
import com.example.clockmallservice.mapper.OrdersInfoMapper;
import com.example.clockmallservice.service.OrdersInfoService;
import org.springframework.stereotype.Service;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 16:39
 * @description
 **/
@Service
public class OrdersInfoServiceImpl extends ServiceImpl<OrdersInfoMapper,OrdersInfo> implements OrdersInfoService {
}
