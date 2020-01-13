package com.example.clockmallservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.clockmallservice.entity.Cause;
import com.example.clockmallservice.mapper.CauseMapper;
import com.example.clockmallservice.service.CauseService;
import org.springframework.stereotype.Service;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 15:19
 * @description
 **/
@Service
public class CauseServiceImpl extends ServiceImpl<CauseMapper,Cause> implements CauseService {
}
