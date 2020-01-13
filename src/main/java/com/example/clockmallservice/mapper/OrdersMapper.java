package com.example.clockmallservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.clockmallservice.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 16:35
 * @description
 **/
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
