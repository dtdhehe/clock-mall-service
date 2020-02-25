package com.example.clockmallservice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.clockmallservice.vo.GoodsVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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

    private String orderCode;

    private String customerId;

    @TableField(exist = false)
    private String customerLoginName;
    @TableField(exist = false)
    private String customerName;
    @TableField(exist = false)
    private String customerPhone;

    private BigDecimal orderAmount;

    private String receiverName;

    private String receiverAddress;

    private String receiverPhone;

    private String deliverySn;

    private Integer status;

    private String causeId;
    @TableField(exist = false)
    private String causeName;

    private String causeDesc;

    @TableField(exist = false)
    private List<GoodsVO> goodsList;

}
