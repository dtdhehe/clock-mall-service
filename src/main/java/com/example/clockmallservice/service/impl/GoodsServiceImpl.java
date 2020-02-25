package com.example.clockmallservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.clockmallservice.entity.Brand;
import com.example.clockmallservice.entity.Goods;
import com.example.clockmallservice.mapper.GoodsMapper;
import com.example.clockmallservice.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 16:00
 * @description
 **/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Goods> implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Override
    public IPage<Goods> queryGoodsList(IPage<Goods> iPage, QueryWrapper<Goods> wrapper) {
        return goodsMapper.queryGoodsList(iPage,wrapper);
    }

    @Override
    public IPage<Goods> queryGoodsListForHome(IPage<Goods> iPage,QueryWrapper<Goods> wrapper) {
        return goodsMapper.queryGoodsListForHome(iPage,wrapper);
    }

    @Override
    public List<Goods> queryHotGoods(Integer limit) {
        return goodsMapper.queryHotGoods(limit);
    }

    @Override
    public List<Brand> queryBrandGoods(QueryWrapper<Brand> wrapper) {
        return goodsMapper.queryBrandGoods(wrapper);
    }
}
