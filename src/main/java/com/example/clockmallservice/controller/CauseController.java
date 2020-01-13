package com.example.clockmallservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.clockmallservice.entity.Cause;
import com.example.clockmallservice.service.CauseService;
import com.example.clockmallservice.util.ConstantUtils;
import com.example.clockmallservice.util.ResultUtils;
import com.example.clockmallservice.vo.ResultVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 15:20
 * @description
 **/
@Api(tags = "退货原因")
@RestController
@RequestMapping("/cause")
public class CauseController {

    @Autowired
    private CauseService causeService;

    /**
     * 新增退货原因
     * @param cause
     * @return
     */
    @PostMapping("")
    public ResultVO addCause(@RequestBody Cause cause){
        return causeService.save(cause)?ResultUtils.success("新增品牌成功"):ResultUtils.failed("新增失败");
    }

    /**
     * 修改退货原因信息
     * @param cause
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResultVO updateCause(@RequestBody Cause cause,@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        if (StringUtils.isEmpty(cause.getId())){
            cause.setId(id);
        }
        return causeService.updateById(cause)?ResultUtils.success("修改退货原因成功"):ResultUtils.failed("修改失败");
    }

    /**
     * 删除退货原因
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVO deleteCause(@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        Cause cause = causeService.getById(id);
        cause.setValidFlag(ConstantUtils.NOTACTIVE);
        return causeService.updateById(cause)?ResultUtils.success("删除退货原因成功"):ResultUtils.failed("删除失败");
    }

    /**
     * 根据id获得退货原因
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultVO getCause(@PathVariable("id") String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        Cause cause = causeService.getById(id);
        return ResultUtils.success("查询成功",cause);
    }

    /**
     * 查询退货原因列表
     * @param queryMap
     * @return
     */
    @GetMapping("/list")
    public ResultVO queryCauseList(@RequestParam Map<String,Object> queryMap){
        QueryWrapper<Cause> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        queryWrapper.orderByDesc("update_time");
        IPage<Cause> iPage = new Page<>( Long.valueOf((String) queryMap.get("page")),Long.valueOf((String) queryMap.get("size")));
        if (!StringUtils.isEmpty(queryMap.get("causeName"))){
            //退货原因
            queryWrapper.like("cause_name",queryMap.get("causeName"));
        }
        iPage = causeService.page(iPage,queryWrapper);
        Map<String,Object> resultMap = new HashMap<>(8);
        resultMap.put("rows",iPage.getRecords());
        resultMap.put("pages",iPage.getPages());
        resultMap.put("total",iPage.getTotal());
        return ResultUtils.success("查询成功",resultMap);
    }

}
