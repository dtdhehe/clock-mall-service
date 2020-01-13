package com.example.clockmallservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.clockmallservice.entity.Address;
import com.example.clockmallservice.mapper.AddressMapper;
import com.example.clockmallservice.service.AddressService;
import org.springframework.stereotype.Service;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 11:14
 * @description
 **/
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper,Address> implements AddressService {
}
