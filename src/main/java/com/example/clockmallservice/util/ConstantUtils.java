package com.example.clockmallservice.util;

import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 包豪娟
 * @version 1.0
 * @date 2019/11/5 23:27
 * @description
 **/
public class ConstantUtils {
    public static final Integer UNKNOWN = -1;
    public static final Integer NOTACTIVE = 0;
    public static final Integer ACTIVE = 1;
    public static final Integer LOCKED = 2;
    public static final Integer ERROR = 0;
    public static final Integer SUCCESS = 1;
    public static final Integer FAILED = 2;
    public static final Integer ADMIN = 0;
    public static final Integer CUSTOMER = 1;

    /**
     * 随机获得主键
     * @return
     */
    public static synchronized String getUniqueKey() {
        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 密码加密
     * @param userPwd
     * @return
     */
    public static String getPassword(String userPwd,String salt){
        return new SimpleHash("MD5",userPwd,salt,1024).toHex();
    }
}
