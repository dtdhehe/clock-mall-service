package com.example.clockmallservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.clockmallservice.entity.*;
import com.example.clockmallservice.service.AddressService;
import com.example.clockmallservice.service.GoodsService;
import com.example.clockmallservice.service.OrdersInfoService;
import com.example.clockmallservice.service.OrdersService;
import com.example.clockmallservice.util.ConstantUtils;
import com.example.clockmallservice.util.DateUtils;
import com.example.clockmallservice.util.RedisUtils;
import com.example.clockmallservice.util.ResultUtils;
import com.example.clockmallservice.vo.CartVO;
import com.example.clockmallservice.vo.OrderResultVO;
import com.example.clockmallservice.vo.OrderVO;
import com.example.clockmallservice.vo.ResultVO;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 16:40
 * @description
 **/
@Api(tags = "订单")
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrdersInfoService infoService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private GoodsService goodsService;
    @Resource
    private RedisUtils redisUtils;
    /**
     * 生成订单编号
     * @return
     */
    private String getOrderCode(){
        String dateTime = DateUtils.formatDateTime3();
        long random = (long) ((Math.random()*9+1)*100000);
        return dateTime + Long.toString(random);
    }

    /**
     * 创建订单
     * @param orderVO
     * @return
     */
    @SuppressWarnings({"unchecked"})
    @PostMapping("")
    public ResultVO createOrders(@RequestBody OrderVO orderVO){
        if (StringUtils.isEmpty(orderVO.getAddressId())){
            return ResultUtils.failed("地址id不能为空");
        }
        Address address = addressService.getById(orderVO.getAddressId());
        //当前登录用户
        User currUser = (User) SecurityUtils.getSubject().getPrincipal();
        //创建订单主表
        BigDecimal orderAmount = BigDecimal.ZERO;
        Orders orders = new Orders();
        orders.setId(ConstantUtils.getUniqueKey());
        orders.setOrderCode(getOrderCode());
        orders.setCustomerId(currUser.getId());
        orders.setReceiverName(address.getReceiverName());
        orders.setReceiverAddress(address.getReceiverAddress() + " " + address.getDetailAddress());
        orders.setReceiverPhone(address.getReceiverPhone());
        orders.setStatus(0);
        //创建订单子表
        String[] itemIdArr = orderVO.getItemIds().split(",");
        for (String item : itemIdArr){
            String[] ids = item.split("-");
            //商品id
            String id = ids[0];
            //商品购买数量
            Integer buyQuantity = Integer.valueOf(ids[1]);
            Goods goods = goodsService.getById(id);
            OrdersInfo info = new OrdersInfo();
            info.setOrderId(orders.getId());
            info.setGoodsId(goods.getId());
            info.setGoodsName(goods.getGoodsName());
            info.setGoodsUrl(goods.getGoodsUrl());
            info.setGoodsPrice(goods.getGoodsPrice());
            info.setBuyQuantity(buyQuantity);
            infoService.save(info);
            //计算总价
            orderAmount = orderAmount.add(goods.getGoodsPrice().multiply(BigDecimal.valueOf(buyQuantity)));
            //购物车数量核减
            List<CartVO> cartList = (List<CartVO>) redisUtils.get(currUser.getId());
            Iterator<CartVO> it = cartList.iterator();
            while (it.hasNext()){
                CartVO cartVOItem = it.next();
                if (cartVOItem.getGoodsId().equals(goods.getId())){
                    //如果购物车中数量大于购买数量
                    if (cartVOItem.getBuyCounts() >= buyQuantity){
                        cartVOItem.setBuyCounts(cartVOItem.getBuyCounts() - buyQuantity);
                    }else {
                        it.remove();
                    }
                }
            }
            redisUtils.set(currUser.getId(),cartList);
        }
        orders.setOrderAmount(orderAmount);
        return ordersService.save(orders)?ResultUtils.success("提交订单成功"):ResultUtils.failed("提交失败");
    }

    /**
     * 管理员查询全部订单
     * @param queryMap
     * @return
     */
    @GetMapping("/list")
    public ResultVO getAllOrders(@RequestParam Map<String,Object> queryMap){
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByAsc("IF(status)");
        queryWrapper.last("ORDER BY IF(t.`status` = 0,0,1), update_time DESC");
//        queryWrapper.orderByDesc("update_time");
        IPage<Orders> iPage = new Page<>( Long.valueOf((String) queryMap.get("page")),Long.valueOf((String) queryMap.get("size")));
        if (!StringUtils.isEmpty(queryMap.get("orderCode"))){
            //订单编号
            queryWrapper.like("order_code",queryMap.get("orderCode"));
        }
        if (!StringUtils.isEmpty(queryMap.get("date"))){
            //日期
            String date = (String) queryMap.get("date");
            queryWrapper.eq("SUBSTR(create_time,1,10)",DateUtils.formatDate(new Date(Long.valueOf(date)),"yyyy-MM-dd"));
        }
        if (!StringUtils.isEmpty(queryMap.get("status"))){
            //订单状态
            queryWrapper.eq("status",queryMap.get("status"));
        }
        iPage = ordersService.getAllOrders(iPage,queryWrapper);
        Map<String,Object> resultMap = new HashMap<>(8);
        resultMap.put("rows",iPage.getRecords());
        resultMap.put("pages",iPage.getPages());
        resultMap.put("total",iPage.getTotal());
        return ResultUtils.success("查询成功",resultMap);
    }

    /**
     * 查询当前登录用户订单
     * @return
     */
    @GetMapping("/my")
    public ResultVO getMyOrders(@RequestParam Map<String,Object> queryMap){
        QueryWrapper<OrderResultVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        User currUser = (User) SecurityUtils.getSubject().getPrincipal();
        queryWrapper.eq("customer_id",currUser.getId());
        queryWrapper.orderByDesc("update_time");
        IPage<OrderResultVO> iPage = new Page<>( Long.valueOf((String) queryMap.get("page")),Long.valueOf((String) queryMap.get("size")));
        if (!StringUtils.isEmpty(queryMap.get("date"))){
            //日期
            queryWrapper.ge("create_time",queryMap.get("date"));
        }
        iPage = ordersService.queryMyOrder(iPage,queryWrapper);
        Map<String,Object> resultMap = new HashMap<>(8);
        resultMap.put("rows",iPage.getRecords());
        resultMap.put("pages",iPage.getPages());
        resultMap.put("total",iPage.getTotal());
        return ResultUtils.success("查询成功",resultMap);
    }

    /**
     * 删除订单
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVO delMyOrders(@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("订单id不能为空");
        }
        //删除订单主表
        Orders orders = ordersService.getById(id);
        orders.setValidFlag(ConstantUtils.NOTACTIVE);
        return ordersService.updateById(orders)?ResultUtils.success("删除订单成功"):ResultUtils.failed("删除失败");
    }

    /**
     * 修改订单状态
     * @param id
     * @param status
     * @return
     */
    @PutMapping("/{id}")
    public ResultVO updateOrderStatus(@PathVariable String id,@RequestParam(value = "status",defaultValue = "0")Integer status){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("id不能为空");
        }
        Orders orders = ordersService.getById(id);
        orders.setStatus(status);
        return ordersService.updateById(orders)?ResultUtils.success("修改订单状态成功"):ResultUtils.failed("修改失败");
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public ResultVO getOrderInfo(@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("id不能为空");
        }
        Orders orderInfo = ordersService.getOrderInfo(id);
        return ResultUtils.success("查询成功",orderInfo);
    }

    /**
     * 填写快递单号
     * @param id
     * @param code
     * @return
     */
    @PutMapping("/delivery/{id}")
    public ResultVO editDeliverySn(@PathVariable String id,@RequestParam("code")String code){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("id不能为空");
        }
        if (StringUtils.isEmpty(code)){
            return ResultUtils.failed("快递单号不能为空");
        }
        Orders orders = ordersService.getById(id);
        orders.setDeliverySn(code);
        orders.setStatus(1);
        return ordersService.updateById(orders)?ResultUtils.success("填写快递单号成功"):ResultUtils.failed("填写失败");
    }

    /**
     * 退货
     * @param id
     * @param causeId
     * @param causeDesc
     * @return
     */
    @PutMapping("/cancel/{id}")
    public ResultVO cancelOrder(@PathVariable String id,@RequestParam("causeId")String causeId,@RequestParam("causeDesc")String causeDesc){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("id不能为空");
        }
        if (StringUtils.isEmpty(causeId)){
            return ResultUtils.failed("退货原因不能为空");
        }
        Orders orders = ordersService.getById(id);
        orders.setCauseId(causeId);
        orders.setCauseDesc(causeDesc);
        orders.setStatus(3);
        return ordersService.updateById(orders)?ResultUtils.success("退货成功"):ResultUtils.failed("退货失败");
    }

}
