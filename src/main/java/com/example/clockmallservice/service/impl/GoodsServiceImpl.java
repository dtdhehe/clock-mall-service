package com.example.clockmallservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.clockmallservice.entity.Goods;
import com.example.clockmallservice.mapper.GoodsMapper;
import com.example.clockmallservice.service.GoodsService;
import org.springframework.stereotype.Service;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 16:00
 * @description
 **/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Goods> implements GoodsService {
}
