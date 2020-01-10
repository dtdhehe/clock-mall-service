package com.example.clockmallservice.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.example.clockmallservice.util.ConstantUtils;
import com.example.clockmallservice.util.DateUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/11/3 17:50
 * @description
 **/
@Component
public class MybatisObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("validFlag",ConstantUtils.ACTIVE,metaObject);
        this.setFieldValByName("createTime",System.currentTimeMillis(),metaObject);
        this.setFieldValByName("updateTime",System.currentTimeMillis(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime",System.currentTimeMillis(),metaObject);
    }
}
