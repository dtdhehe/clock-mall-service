package com.example.clockmallservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.clockmallservice.entity.Brand;
import com.example.clockmallservice.entity.Goods;

import java.util.List;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 15:59
 * @description
 **/
public interface GoodsService extends IService<Goods> {

    /**
     * 查询商品列表
     * @param iPage
     * @param wrapper
     * @return
     */
    IPage<Goods> queryGoodsList(IPage<Goods> iPage, QueryWrapper<Goods> wrapper);

    /**
     * 查询首页商品
     * @param wrapper
     * @return
     */
    IPage<Goods> queryGoodsListForHome(IPage<Goods> iPage,QueryWrapper<Goods> wrapper);

    /**
     * 查询热门商品
     * @param limit
     * @return
     */
    List<Goods> queryHotGoods(Integer limit);

    /**
     * 首页按品牌查询商品
     * @param wrapper
     * @return
     */
    List<Brand> queryBrandGoods(QueryWrapper<Brand> wrapper);

}
