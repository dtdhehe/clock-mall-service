package com.example.clockmallservice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/11/6 9:52
 * @description
 **/
@Data
public class User {

    @TableId(type = IdType.UUID)
    private String id;
    @TableField(fill = FieldFill.INSERT)
    private Integer validFlag;
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    private String loginName;

    private String password;

    private String name;

    private String phone;

    private String sex;

    private Integer userType;

    private String userImgUrl;

}
