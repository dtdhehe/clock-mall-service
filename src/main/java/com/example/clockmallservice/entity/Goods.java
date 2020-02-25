package com.example.clockmallservice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2020/1/10 16:18
 * @description
 **/
@Data
public class Goods {

    @TableId(type = IdType.UUID)
    private String id;
    @TableField(fill = FieldFill.INSERT)
    private Integer validFlag;
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    private String categoryCode;

    @TableField(exist = false)
    private String categoryName;

    private String goodsCode;

    private String goodsName;

    private String goodsDesc;

    private BigDecimal goodsPrice;

    private Integer status;

    private String goodsUrl;

    private Integer goodsInventory;

    private String brandId;

    @TableField(exist = false)
    private String brandName;
    @TableField(exist = false)
    private String brandType;

    private String goodsAttr;

    private Integer goodsLimit;

    @TableField(exist = false)
    private String goodsCount;

}
