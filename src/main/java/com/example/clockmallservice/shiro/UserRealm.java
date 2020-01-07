package com.example.clockmallservice.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.clockmallservice.entity.User;
import com.example.clockmallservice.mapper.UserMapper;
import com.example.clockmallservice.util.ConstantUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 包豪娟
 * @version 1.0
 * @date 2019/11/05 20:49
 * @description
 **/
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("进行授权");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principalCollection.getPrimaryPrincipal();
        //授权
        if (ConstantUtils.ADMIN.equals(user.getUserType())){
            authorizationInfo.addRole("admin");
        }else if (ConstantUtils.CUSTOMER.equals(user.getUserType())){
            authorizationInfo.addRole("sales");
        }
        return authorizationInfo;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("进行认证");
        String userName = (String) authenticationToken.getPrincipal();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name",userName);
        User dbUser = userMapper.selectOne(queryWrapper);
        if (dbUser == null){
            //用户不存在
            throw new UnknownAccountException();
        }
        if (ConstantUtils.NOTACTIVE.equals(dbUser.getValidFlag())){
            throw new DisabledAccountException();
        }
        if (ConstantUtils.LOCKED.equals(dbUser.getValidFlag())){
            throw new LockedAccountException();
        }
        return new SimpleAuthenticationInfo(dbUser,dbUser.getPassword(), ByteSource.Util.bytes(dbUser.getLoginName()),getName());
    }
}
