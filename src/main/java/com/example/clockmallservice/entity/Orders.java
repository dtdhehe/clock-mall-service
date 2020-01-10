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
 * @date 2019/12/10 16:22
 * @description
 **/
@Data
public class Orders {

    @TableId(type = IdType.UUID)
    private String id;
    @TableField(fill = FieldFill.INSERT)
    private Integer validFlag;
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    private Integer orderCode;

    private String customerId;

    private BigDecimal orderAmount;

    private String addressId;

    private String deliverySn;

    private Integer status;

    private String causeId;

    private String causeDesc;

}
