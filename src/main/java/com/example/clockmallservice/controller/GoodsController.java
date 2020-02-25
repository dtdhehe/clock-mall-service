package com.example.clockmallservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.clockmallservice.entity.Brand;
import com.example.clockmallservice.entity.Goods;
import com.example.clockmallservice.service.BrandService;
import com.example.clockmallservice.service.GoodsService;
import com.example.clockmallservice.util.ConstantUtils;
import com.example.clockmallservice.util.ResultUtils;
import com.example.clockmallservice.util.SortEnum;
import com.example.clockmallservice.vo.ResultVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 16:01
 * @description
 **/
@Api(tags = "商品")
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private BrandService brandService;

    /**
     * 新增商品
     * @param goods
     * @return
     */
    @PostMapping("")
    public ResultVO addGoods(@RequestBody Goods goods){
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        wrapper.eq("goods_code",goods.getGoodsCode());
        Goods dbGoods = goodsService.getOne(wrapper);
        if (dbGoods != null){
            return ResultUtils.failed("该商品已存在");
        }
        return goodsService.save(goods)?ResultUtils.success("新增商品成功"):ResultUtils.failed("新增失败");
    }

    /**
     * 修改商品信息
     * @param goods
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResultVO updateGoods(@RequestBody Goods goods,@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        if (StringUtils.isEmpty(goods.getId())){
            goods.setId(id);
        }
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        wrapper.eq("goods_code",goods.getGoodsCode());
        Goods dbGoods = goodsService.getOne(wrapper);
        if (dbGoods != null && !dbGoods.getId().equals(id)){
            return ResultUtils.failed("该商品已存在");
        }
        return goodsService.updateById(goods)?ResultUtils.success("修改商品信息成功"):ResultUtils.failed("修改失败");
    }

    /**
     * 删除商品
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVO deleteGoods(@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        Goods goods = goodsService.getById(id);
        goods.setValidFlag(ConstantUtils.NOTACTIVE);
        return goodsService.updateById(goods)?ResultUtils.success("删除商品成功"):ResultUtils.failed("删除失败");
    }

    /**
     * 根据id获得商品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultVO getGoods(@PathVariable("id") String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        Goods goods = goodsService.getById(id);
        //查询商品品牌相关信息
        Brand brand = brandService.getById(goods.getBrandId());
        if (brand == null){
            return ResultUtils.failed("品牌信息错误");
        }
        goods.setBrandName(brand.getBrandName());
        goods.setBrandType(brand.getBrandType());
        return ResultUtils.success("查询成功",goods);
    }

    /**
     * 查询商品列表
     * @param queryMap
     * @return
     */
    @GetMapping("/list")
    public ResultVO queryGoodsList(@RequestParam Map<String,Object> queryMap){
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("t.valid_flag",ConstantUtils.ACTIVE);
        queryWrapper.orderByAsc("t.category_code");
        IPage<Goods> iPage = new Page<>( Long.valueOf((String) queryMap.get("page")),Long.valueOf((String) queryMap.get("size")));
        if (!StringUtils.isEmpty(queryMap.get("categoryCode"))){
            //类别编码
            queryWrapper.like("t.category_code",queryMap.get("categoryCode"));
        }
        if (!StringUtils.isEmpty(queryMap.get("goodsCode"))){
            //商品编码
            queryWrapper.like("t.goods_code",queryMap.get("goodsCode"));
        }
        if (!StringUtils.isEmpty(queryMap.get("goodsName"))){
            //商品名称
            queryWrapper.like("t.goods_name",queryMap.get("goodsName"));
        }
        if (!StringUtils.isEmpty(queryMap.get("status"))){
            //发布状态
            queryWrapper.eq("t.status",queryMap.get("status"));
        }
        if (!StringUtils.isEmpty(queryMap.get("brandId"))){
            //品牌id
            queryWrapper.eq("t.brand_id",queryMap.get("brandId"));
        }
        iPage = goodsService.queryGoodsList(iPage,queryWrapper);
        Map<String,Object> resultMap = new HashMap<>(8);
        resultMap.put("rows",iPage.getRecords());
        resultMap.put("pages",iPage.getPages());
        resultMap.put("total",iPage.getTotal());
        return ResultUtils.success("查询成功",resultMap);
    }

    /**
     * 商城首页商品查询
     * @param queryMap
     * @return
     */
    @GetMapping("/home/list")
    public ResultVO getHomeGoods(@RequestParam Map<String,Object> queryMap){
        //商品搜索条件
        String desc = (String) queryMap.get("desc");
        //排序方式
        Integer sortType = Integer.valueOf((String)queryMap.get("sortType"));
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        IPage<Goods> iPage = new Page<>( Long.valueOf((String) queryMap.get("page")),Long.valueOf((String) queryMap.get("size")));
        queryWrapper.eq("t.valid_flag",ConstantUtils.ACTIVE);
        //商品类别
        String categoryCode = (String) queryMap.get("categoryCode");
        if (!StringUtils.isEmpty(categoryCode)){
            queryWrapper.eq("t.category_code",categoryCode);
        }
        if (!StringUtils.isEmpty(desc)){
            //搜索条件不为空
            queryWrapper.like("t.goods_name",desc).or().like("t.goods_desc",desc);
        }
        if (!StringUtils.isEmpty(queryMap.get("amountStart"))){
            //价格起
            queryWrapper.ge("t.goods_price",queryMap.get("amountStart"));
        }
        if (!StringUtils.isEmpty(queryMap.get("amountEnd"))){
            //价格止
            queryWrapper.le("t.goods_price",queryMap.get("amountEnd"));
        }
        if (sortType.equals(SortEnum.DEFAULT.getType())){
            //默认排序
            queryWrapper.orderByDesc("t.update_time");
        }
        if (sortType.equals(SortEnum.AMOUNT_ASC.getType())){
            //价格升序
            queryWrapper.orderByAsc("t.goods_price");
        }
        if (sortType.equals(SortEnum.AMOUNT_DESC.getType())){
            //价格降序
            queryWrapper.orderByDesc("t.goods_price");
        }
        iPage = goodsService.queryGoodsListForHome(iPage,queryWrapper);
        Map<String,Object> resultMap = new HashMap<>(8);
        resultMap.put("rows",iPage.getRecords());
        resultMap.put("pages",iPage.getPages());
        resultMap.put("total",iPage.getTotal());
        return ResultUtils.success("查询成功",resultMap);
    }

    /**
     * 查询热门商品
     * @param limit
     * @return
     */
    @GetMapping("/hot")
    public ResultVO getHotGoods(@RequestParam("limit") Integer limit){
        if (limit == null || limit < 0){
            limit = 0;
        }
        List<Goods> goodsList = goodsService.queryHotGoods(limit);
        return ResultUtils.success("查询成功",goodsList);
    }

    /**
     * 按品牌查询最新5条商品
     * @return
     */
    @GetMapping("/brand/list")
    public ResultVO getBrandGoods(){
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        List<Brand> brandList = goodsService.queryBrandGoods(queryWrapper);
        return ResultUtils.success("查询成功",brandList);
    }

}
