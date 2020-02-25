package com.example.clockmallservice.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2020/1/17 20:58
 * @description
 **/
@Data
public class CartVO implements Serializable {

    public static final long serialVersionUID = 1L;
    private String customerId;
    private String goodsId;
    private String goodsName;
    private String goodsUrl;
    private Integer buyCounts;
    private BigDecimal goodsPrice;
    private BigDecimal priceDiscount;

}
