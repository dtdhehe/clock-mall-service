package com.example.clockmallservice.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2020/1/18 21:29
 * @description
 **/
@Data
public class GoodsVO {

    private String goodsName;
    private String goodsUrl;
    private BigDecimal goodsPrice;
    private Integer buyQuantity;
    private String brandName;

}
