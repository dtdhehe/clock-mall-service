package com.example.clockmallservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.clockmallservice.entity.Goods;
import com.example.clockmallservice.entity.Orders;
import com.example.clockmallservice.entity.User;
import com.example.clockmallservice.service.GoodsService;
import com.example.clockmallservice.service.OrdersService;
import com.example.clockmallservice.service.UserService;
import com.example.clockmallservice.util.ConstantUtils;
import com.example.clockmallservice.util.DateUtils;
import com.example.clockmallservice.util.ResultUtils;
import com.example.clockmallservice.vo.ResultVO;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/11/6 10:15
 * @description
 **/
@Api(tags = "登录")
@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserService userService;
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/")
    public ResultVO unLogin(){
        return ResultUtils.unauth("unLogin");
    }

    /**
     * 登录页面
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResultVO login(@RequestBody User user){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(),user.getPassword());
        try {
            subject.login(token);
            return ResultUtils.success("login",subject.getPrincipal());
        }catch (UnknownAccountException e){
            e.printStackTrace();
            return ResultUtils.failed("用户名不存在");
        }catch (DisabledAccountException e){
            e.printStackTrace();
            return ResultUtils.failed("该用户未激活");
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            return ResultUtils.failed("密码错误");
        }
    }

    @GetMapping("/home")
    public ResultVO getHomeData(){
        //订单总数
        int orderCount = ordersService.count();
        //退款订单
        QueryWrapper<Orders> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq("status",3).or().eq("status",4);
        int cancelOrderCount = ordersService.count(orderWrapper);
        //商品总数
        QueryWrapper<Goods> goodsWrapper = new QueryWrapper<>();
        goodsWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        int goodsCount = goodsService.count(goodsWrapper);
        //商品上架数
        goodsWrapper.eq("status",1);
        int saleGoodsCount = goodsService.count(goodsWrapper);
        //商品下架数
        int unSaleGoodsCount = goodsCount - saleGoodsCount;
        //会员总数
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        userWrapper.eq("user_type",ConstantUtils.ACTIVE);
        int userCount = userService.count(userWrapper);
        //当日新增
        userWrapper.likeRight("create_time",DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
        int todayUserCount = userService.count(userWrapper);
        //当月新增
        QueryWrapper<User> monthWrapper = new QueryWrapper<>();
        monthWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        monthWrapper.eq("user_type",ConstantUtils.ACTIVE);
        monthWrapper.likeRight("create_time",DateUtils.formatDate(new Date(),"yyyy-MM-dd").substring(0,7));
        int monthUserCount = userService.count(monthWrapper);

        Map<String,Object> resultMap = new HashMap<>(16);
        resultMap.put("orderCount",orderCount);
        resultMap.put("cancelOrderCount",cancelOrderCount);
        resultMap.put("goodsCount",goodsCount);
        resultMap.put("saleGoodsCount",saleGoodsCount);
        resultMap.put("unSaleGoodsCount",unSaleGoodsCount);
        resultMap.put("userCount",userCount);
        resultMap.put("todayUserCount",todayUserCount);
        resultMap.put("monthUserCount",monthUserCount);
        return ResultUtils.success("查询成功",resultMap);
    }

}
