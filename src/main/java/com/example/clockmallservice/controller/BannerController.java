package com.example.clockmallservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.clockmallservice.entity.Banner;
import com.example.clockmallservice.service.BannerService;
import com.example.clockmallservice.util.ConstantUtils;
import com.example.clockmallservice.util.ResultUtils;
import com.example.clockmallservice.vo.ResultVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/10 16:31
 * @description
 **/
@Api(tags = "轮播图")
@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * 新增轮播图
     * @param banner
     * @return
     */
    @PostMapping("")
    public ResultVO addBanner(@RequestBody Banner banner){
        return bannerService.save(banner)?ResultUtils.success("新增轮播图成功"):ResultUtils.failed("新增失败");
    }

    /**
     * 修改轮播图
     * @param banner
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResultVO updateBanner(@RequestBody Banner banner,@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        if (StringUtils.isEmpty(banner.getId())){
            banner.setId(id);
        }
        return bannerService.updateById(banner)?ResultUtils.success("修改轮播图信息成功"):ResultUtils.failed("修改失败");
    }

    /**
     * 删除轮播图
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVO deleteBanner(@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        Banner banner = bannerService.getById(id);
        banner.setValidFlag(ConstantUtils.NOTACTIVE);
        return bannerService.updateById(banner)?ResultUtils.success("删除轮播图成功"):ResultUtils.failed("删除失败");
    }

    /**
     * 根据id获得轮播图
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultVO getBanner(@PathVariable("id") String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        Banner banner = bannerService.getById(id);
        return ResultUtils.success("查询成功",banner);
    }

    /**
     * 查询轮播图列表
     * @param queryMap
     * @return
     */
    @GetMapping("/list")
    public ResultVO queryBannerList(@RequestParam Map<String,Object> queryMap){
        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        queryWrapper.orderByAsc("sort");
        IPage<Banner> iPage = new Page<>( Long.valueOf((String) queryMap.get("page")),Long.valueOf((String) queryMap.get("size")));
        if (!StringUtils.isEmpty(queryMap.get("bannerName"))){
            //轮播图名字
            queryWrapper.like("banner_name",queryMap.get("bannerName"));
        }
        if (!StringUtils.isEmpty(queryMap.get("status"))){
            //是否启用
            queryWrapper.eq("status",queryMap.get("status"));
        }
        iPage = bannerService.page(iPage,queryWrapper);
        Map<String,Object> resultMap = new HashMap<>(8);
        resultMap.put("rows",iPage.getRecords());
        resultMap.put("pages",iPage.getPages());
        resultMap.put("total",iPage.getTotal());
        return ResultUtils.success("查询成功",resultMap);
    }

    /**
     * 上传轮播图
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResultVO upload(MultipartFile file){
        File f = new File("D:/uploads");
        StringBuilder url = new StringBuilder();
        if (!f.exists()){
            f.mkdirs();
        }
        try {
            String fileName = file.getOriginalFilename();
            url.append(System.currentTimeMillis()).append(fileName);
            //新文件路径
            String resultUrl = "D:/uploads/"+ url;
            File upFile = new File(resultUrl);
            file.transferTo(upFile);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtils.failed("上传失败");
        }
        //因为浏览器原因，设置虚拟路径为   /uploads/
        return ResultUtils.success("上传成功","http://localhost:9595/uploads/"+url);
    }

}
