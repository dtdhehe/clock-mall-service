package com.example.clockmallservice.controller;

import com.example.clockmallservice.entity.User;
import com.example.clockmallservice.util.RedisUtils;
import com.example.clockmallservice.util.ResultUtils;
import com.example.clockmallservice.vo.CartVO;
import com.example.clockmallservice.vo.ResultVO;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2020/1/17 21:12
 * @description
 **/
@Api(tags = "购物车")
@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private RedisUtils redisUtils;

    /**
     * 添加商品到购物车
     * @param cartVO
     * @return
     */
    @SuppressWarnings({"unchecked"})
    @PostMapping("")
    public ResultVO saveCart(@RequestBody CartVO cartVO){
        User currUser = (User)SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isEmpty(cartVO.getCustomerId())){
            cartVO.setCustomerId(currUser.getId());
        }
        //该商品小计
        if (StringUtils.isEmpty(cartVO.getPriceDiscount())){
            cartVO.setPriceDiscount(cartVO.getGoodsPrice().multiply(BigDecimal.valueOf(cartVO.getBuyCounts())));
        }
        //通过redis拿到当前用户的购物车
        List<CartVO> cartVOList = (List<CartVO>) redisUtils.get(currUser.getId());
        if (cartVOList == null || cartVOList.size() == 0){
            //购物车为空
            cartVOList = new ArrayList<>();
            cartVOList.add(cartVO);
            redisUtils.set(currUser.getId(),cartVOList);
        }else {
            //购物车不为空
            //判断购物车中是否有该商品
            boolean isFlag = false;
            for (CartVO item : cartVOList){
                if (item.getGoodsId().equals(cartVO.getGoodsId())){
                    item.setBuyCounts(item.getBuyCounts() + cartVO.getBuyCounts());
                    isFlag = true;
                }
            }
            if (!isFlag){
                cartVOList.add(cartVO);
            }
            redisUtils.set(currUser.getId(),cartVOList);
        }
        return ResultUtils.success("加入购物车成功");
    }

    /**
     * 查询当前登录用户购物车
     * @return
     */
    @SuppressWarnings({"unchecked"})
    @GetMapping("/list")
    public ResultVO getCart(){
        User currUser = (User)SecurityUtils.getSubject().getPrincipal();
        List<CartVO> cartList = getCurrentCartList(currUser.getId());
        System.out.println(cartList);
        return ResultUtils.success("查询成功",cartList);
    }

    /**
     * 购物车商品数量修改
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResultVO subCart(@PathVariable String id,@RequestParam("num")Integer num){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("id不能为空");
        }
        User currUser = (User)SecurityUtils.getSubject().getPrincipal();
        List<CartVO> cartList = getCurrentCartList(currUser.getId());
        for (CartVO item : cartList){
            if (item.getGoodsId().equals(id)){
                item.setBuyCounts(num);
            }
        }
        redisUtils.set(currUser.getId(),cartList);
        return ResultUtils.success("购物车数量修改成功");
    }

    /**
     * 删除购物车中指定商品
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVO removeCart(@PathVariable String id) {
        if (StringUtils.isEmpty(id)) {
            return ResultUtils.failed("id不能为空");
        }
        User currUser = (User) SecurityUtils.getSubject().getPrincipal();
        List<CartVO> cartList = getCurrentCartList(currUser.getId());
        cartList.removeIf(item -> item.getGoodsId().equals(id));
        redisUtils.set(currUser.getId(),cartList);
        return ResultUtils.success("删除购物车商品成功");
    }

    /**
     * 获得指定用户id的购物车
     * @param id
     * @return
     */
    @SuppressWarnings({"unchecked"})
    private List<CartVO> getCurrentCartList(String id){
        return (List<CartVO>) redisUtils.get(id);
    }
}
