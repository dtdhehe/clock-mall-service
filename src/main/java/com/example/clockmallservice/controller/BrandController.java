package com.example.clockmallservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.clockmallservice.entity.Brand;
import com.example.clockmallservice.service.BrandService;
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
 * @date 2019/12/13 14:27
 * @description
 **/
@Api(tags = "品牌")
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 新增品牌
     * @param brand
     * @return
     */
    @PostMapping("")
    public ResultVO addBrand(@RequestBody Brand brand){
        return brandService.save(brand)?ResultUtils.success("新增品牌成功"):ResultUtils.failed("新增失败");
    }

    /**
     * 修改品牌信息
     * @param brand
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResultVO updateBrand(@RequestBody Brand brand,@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        if (StringUtils.isEmpty(brand.getId())){
            brand.setId(id);
        }
        return brandService.updateById(brand)?ResultUtils.success("修改品牌信息成功"):ResultUtils.failed("修改失败");
    }

    /**
     * 删除品牌
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVO deleteBrand(@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        Brand brand = brandService.getById(id);
        brand.setValidFlag(ConstantUtils.NOTACTIVE);
        return brandService.updateById(brand)?ResultUtils.success("删除品牌成功"):ResultUtils.failed("删除失败");
    }

    /**
     * 根据id获得品牌信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultVO getBrand(@PathVariable("id") String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        Brand brand = brandService.getById(id);
        return ResultUtils.success("查询成功",brand);
    }

    /**
     * 查询品牌列表
     * @param queryMap
     * @return
     */
    @GetMapping("/list")
    public ResultVO queryBrandList(@RequestParam Map<String,Object> queryMap){
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        queryWrapper.orderByAsc("brand_letter");
        IPage<Brand> iPage = new Page<>( Long.valueOf((String) queryMap.get("page")),Long.valueOf((String) queryMap.get("size")));
        if (!StringUtils.isEmpty(queryMap.get("brandName"))){
            //轮播图名字
            queryWrapper.like("brand_name",queryMap.get("brandName"));
        }
        iPage = brandService.page(iPage,queryWrapper);
        Map<String,Object> resultMap = new HashMap<>(8);
        resultMap.put("rows",iPage.getRecords());
        resultMap.put("pages",iPage.getPages());
        resultMap.put("total",iPage.getTotal());
        return ResultUtils.success("查询成功",resultMap);
    }

}
