package com.example.clockmallservice.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.example.clockmallservice.entity.Orders;
import com.example.clockmallservice.vo.OrderResultVO;
import org.apache.ibatis.annotations.*;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 16:35
 * @description
 **/
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {


    @Select("select * from orders ${ew.customSqlSegment}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "goodsList",column = "id",
                    many = @Many(select = "com.example.clockmallservice.mapper.OrdersInfoMapper.queryByOrderId"))
    })
    IPage<OrderResultVO> queryMyOrder(IPage<OrderResultVO> iPage, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select t.*,u.name customer_name from orders t left join user u on t.customer_id = u.id ${ew.customSqlSegment}")
    IPage<Orders> getAllOrders(IPage<Orders> iPage, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select t.*,u.login_name customer_login_name,u.name customer_name,u.phone customer_phone,c.cause_name cause_name from orders t " +
            "left join user u on t.customer_id = u.id left join cause c on t.cause_id = c.id where t.id = #{id}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "goodsList",column = "id",
                    many = @Many(select = "com.example.clockmallservice.mapper.OrdersInfoMapper.queryByOrderId"))
    })
    Orders getOrderInfo(@Param("id")String id);

}
