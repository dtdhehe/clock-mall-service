package com.example.clockmallservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.clockmallservice.entity.Category;
import com.example.clockmallservice.service.CategoryService;
import com.example.clockmallservice.util.ConstantUtils;
import com.example.clockmallservice.util.ResultUtils;
import com.example.clockmallservice.vo.ResultVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 14:37
 * @description
 **/
@Api(tags = "商品类别")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增商品类别
     * @param category
     * @return
     */
    @PostMapping("")
    public ResultVO addCategory(@RequestBody Category category){
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        if (!StringUtils.isEmpty(category.getCategoryPcode())){
            //父编码不为空，则为新增二级类别
            wrapper.eq("category_pcode",category.getCategoryPcode());
            wrapper.orderByDesc("category_code");
            List<Category> categoryList = categoryService.list(wrapper);
            //当前类别编码
            category.setCategoryCode(category.getCategoryPcode()+"01");
            if (categoryList.size() != 0){
                //二级类别取编码后两位计算
                String currCode = (Integer.valueOf(categoryList.get(0).getCategoryCode().substring(2)) + 1) + "";
                if (currCode.length() == 1){
                    currCode = "0"+currCode;
                }
                category.setCategoryCode(category.getCategoryPcode()+currCode);
            }
        }else {
            //父编码为空，新增一级类别
            wrapper.isNull("category_pcode");
            wrapper.orderByDesc("category_code");
            List<Category> supplierList = categoryService.list(wrapper);
            //当前类别编码
            //第一个类别
            category.setCategoryCode("01");
            if (supplierList.size() != 0){
                String currCode = (Integer.valueOf(supplierList.get(0).getCategoryCode()) + 1) + "";
                if (currCode.length() == 1){
                    currCode = "0"+currCode;
                }
                category.setCategoryCode(currCode);
            }
        }
        return categoryService.save(category)?ResultUtils.success("新增类别成功"):ResultUtils.failed("新增失败");
    }

    /**
     * 修改品牌信息
     * @param category
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResultVO updateCategory(@RequestBody Category category,@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        if (StringUtils.isEmpty(category.getId())){
            category.setId(id);
        }
        return categoryService.updateById(category)?ResultUtils.success("修改类别信息成功"):ResultUtils.failed("修改失败");
    }

    /**
     * 删除类别
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVO deleteCategory(@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        Category category = categoryService.getById(id);
        category.setValidFlag(ConstantUtils.NOTACTIVE);
        return categoryService.updateById(category)?ResultUtils.success("删除类别成功"):ResultUtils.failed("删除失败");
    }

    /**
     * 根据id获得类别信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultVO getCategory(@PathVariable("id") String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        Category category = categoryService.getById(id);
        return ResultUtils.success("查询成功",category);
    }

    /**
     * 根据code获得类别信息
     * @param code
     * @return
     */
    @GetMapping("/code/{code}")
    public ResultVO getCategoryByCode(@PathVariable("code") String code){
        if (StringUtils.isEmpty(code)){
            return ResultUtils.failed("传入的id不能为空");
        }
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        queryWrapper.eq("category_code",code);
        Category category = categoryService.getOne(queryWrapper);
        return ResultUtils.success("查询成功",category);
    }

    /**
     * 查询类别列表
     * @return
     */
    @GetMapping("/list")
    public ResultVO queryCategoryList(){
        List<Map<String,Object>> resultList = new ArrayList<>();
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        queryWrapper.orderByAsc("category_code");
        List<Category> categoryList = categoryService.list(queryWrapper);
        for (Category item : categoryList) {
            if (item.getCategoryCode().length() > 2) {
                //二级类别
                Optional<Map<String, Object>> optional = resultList.stream().filter(e -> e.get("categoryCode").equals(item.getCategoryPcode())).findFirst();
                if (optional.isPresent()){
                    Map<String, Object> parentMap = optional.get();
                    Map<String,Object> map = new HashMap<>(8);
                    map.put("id",item.getId());
                    map.put("categoryName",item.getCategoryName());
                    map.put("categoryDesc",item.getCategoryDesc());
                    map.put("categoryCode",item.getCategoryCode());
                    map.put("categoryPcode",item.getCategoryPcode());
                    ((List)parentMap.get("children")).add(map);
                }
            }else {
                //一级类别
                Map<String,Object> map = new HashMap<>(8);
                map.put("id",item.getId());
                map.put("categoryName",item.getCategoryName());
                map.put("categoryDesc",item.getCategoryDesc());
                map.put("categoryCode",item.getCategoryCode());
                List<Map<String,Object>> childrenList = new ArrayList<>();
                map.put("children",childrenList);
                resultList.add(map);
            }
        }
        return ResultUtils.success("查询成功",resultList);
    }

    /**
     * 根据父类别code获得该类别下所有子类别
     * @param code
     * @return
     */
    @GetMapping("/children/{code}")
    public ResultVO getChildren(@PathVariable("code") String code){
        if (StringUtils.isEmpty(code)){
            return ResultUtils.failed("传入的类别编码不能为空");
        }
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        if (code.length() > 2){
            queryWrapper.eq("category_code",code);
        }else {
            queryWrapper.eq("category_pcode",code);
        }
        queryWrapper.orderByAsc("category_code");
        List<Category> categoryList = categoryService.list(queryWrapper);
        return ResultUtils.success("查询成功",categoryList);
    }

}
