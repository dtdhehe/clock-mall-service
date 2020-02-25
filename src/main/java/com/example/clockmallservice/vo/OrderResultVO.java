package com.example.clockmallservice.vo;

import com.example.clockmallservice.entity.Goods;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2020/1/18 21:13
 * @description
 **/
@Data
public class OrderResultVO {

    private List<GoodsVO> goodsList;
    private String id;
    private String createTime;
    private String orderCode;
    private BigDecimal orderAmount;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private Integer status;

}
