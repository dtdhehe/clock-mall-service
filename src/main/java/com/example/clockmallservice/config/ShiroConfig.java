package com.example.clockmallservice.config;

import com.example.clockmallservice.shiro.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 包豪娟
 * @version 1.0
 * @date 2019/11/5 20:49
 * @description shiro配置
 **/
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager")DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        factoryBean.setSecurityManager(securityManager);
        //添加内置过滤器
        /* anon:表示可以匿名使用。
           authc:表示需要认证(登录)才能使用，没有参数
           roles：参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，当有多个参数时，例如admins/user/**=roles["admin,guest"],每个参数通过才算通过，相当于hasAllRoles()方法。
           perms：参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，例如/admins/user/**=perms["user:add:*,user:modify:*"]，当有多个参数时必须每个参数都通过才通过，想当于isPermitedAll()方法。
           rest：根据请求的方法，相当于/admins/user/**=perms[user:method] ,其中method为post，get，delete等。
           port：当请求的url的端口不是8081是跳转到schemal://serverName:8081?queryString,其中schmal是协议http或https等，serverName是你访问的host,8081是url配置里port的端口，queryString是你访问的url里的？后面的参数。
           authcBasic：没有参数表示httpBasic认证
           ssl:表示安全的url请求，协议为https
           user:当登入操作时不做检查*/
        Map<String,String> map = new LinkedHashMap<>();
        //-------swagger接口权限 开放-------
        map.put("/swagger-ui.html","anon");
        map.put("/webjars/**", "anon");
        map.put("/v2/**", "anon");
        map.put("/swagger-resources/**", "anon");
        //-------swagger接口权限 开放-------
        //对静态资源放行
        map.put("/static/**","anon");
        map.put("/index","anon");
        map.put("/user","anon");
        map.put("/login","anon");
        map.put("/uploads/**","anon");
        //商城首页需要拦截的url
        map.put("/banner/list","anon");
        map.put("/goods/**","anon");
        map.put("/category/list","anon");
        //退出登录
        map.put("/logout","logout");
        map.put("/**","authc");
        //被拦截的登录页面
        factoryBean.setLoginUrl("/");
        //未授权页面
        factoryBean.setUnauthorizedUrl("");
        factoryBean.setFilterChainDefinitionMap(map);
        return factoryBean;
    }

    /**
     * 创建DefaultWebSecurityManager
     * @param userRealm
     * @return
     */
    @Bean(name = "defaultWebSecurityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm")UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    /**
     * 创建realm
     * @return
     */
    @Bean(name = "userRealm")
    public UserRealm getUserRealm(@Qualifier("hashedCredentialsMatcher")HashedCredentialsMatcher matcher){
        UserRealm realm = new UserRealm();
        realm.setCredentialsMatcher(matcher);
        return realm;
    }

    /**
     * 密码匹配凭证管理器
     * @return
     */
    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //采用MD5方式加密
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //设置加密次数
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }

}
