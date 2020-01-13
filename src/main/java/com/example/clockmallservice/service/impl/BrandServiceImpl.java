package com.example.clockmallservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.clockmallservice.entity.Brand;
import com.example.clockmallservice.mapper.BrandMapper;
import com.example.clockmallservice.service.BrandService;
import org.springframework.stereotype.Service;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 14:26
 * @description
 **/
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper,Brand> implements BrandService {
}
