package com.example.clockmallservice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/10 16:09
 * @description
 **/
@Data
public class Address {

    @TableId(type = IdType.UUID)
    private String id;
    @TableField(fill = FieldFill.INSERT)
    private Integer validFlag;
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    private String selectedOptions;

    private String customerId;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private String detailAddress;

    private Integer isDefault;

}
