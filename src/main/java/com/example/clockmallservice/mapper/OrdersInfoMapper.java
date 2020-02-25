package com.example.clockmallservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.clockmallservice.entity.OrdersInfo;
import com.example.clockmallservice.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 16:38
 * @description
 **/
@Mapper
public interface OrdersInfoMapper extends BaseMapper<OrdersInfo> {

    @Select("select t.goods_name,t.goods_url,t.goods_price,t.buy_quantity,b.brand_name from orders_info t " +
            "left join goods g on t.goods_id = g.id left join brand b on g.brand_id = b.id where t.order_id=#{orderId}")
    GoodsVO queryByOrderId(@Param("orderId") String orderId);

}
