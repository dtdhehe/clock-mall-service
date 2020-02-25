package com.example.clockmallservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.clockmallservice.entity.User;
import com.example.clockmallservice.service.UserService;
import com.example.clockmallservice.util.ConstantUtils;
import com.example.clockmallservice.util.ResultUtils;
import com.example.clockmallservice.vo.ResultVO;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
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
 * @date 2019/12/10 10:26
 * @description
 **/
@Api(tags = "会员")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 新增会员
     * @param user
     * @return
     */
    @PostMapping("")
    public ResultVO addUser(@RequestBody User user){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name",user.getLoginName());
        queryWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        User dbUser = userService.getOne(queryWrapper);
        if (dbUser != null){
            return ResultUtils.failed("该用户名已被使用");
        }
        //密码加密
        user.setPassword(ConstantUtils.getPassword(user.getPassword(),user.getLoginName()));
        return userService.save(user)?ResultUtils.success("新增会员成功"):ResultUtils.failed("新增失败");
    }

    /**
     *修改会员
     * @param user
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResultVO updateUser(@RequestBody User user,@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        if (StringUtils.isEmpty(user.getId())){
            user.setId(id);
        }
        if (!StringUtils.isEmpty(user.getLoginName())){
            //密码加密
            user.setPassword(ConstantUtils.getPassword(user.getPassword(),user.getLoginName()));
        }
        return userService.updateById(user)?ResultUtils.success("修改会员信息成功"):ResultUtils.failed("修改失败");
    }

    /**
     * 删除会员
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVO deleteUser(@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        User user = userService.getById(id);
        user.setValidFlag(ConstantUtils.NOTACTIVE);
        return userService.updateById(user)?ResultUtils.success("删除会员成功"):ResultUtils.failed("删除失败");
    }

    /**
     * 根据id获得会员
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultVO getUser(@PathVariable("id") String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        User user = userService.getById(id);
        return ResultUtils.success("查询成功",user);
    }

    /**
     * 查询会员列表
     * @param queryMap
     * @return
     */
    @GetMapping("/list")
    public ResultVO queryUserList(@RequestParam Map<String,Object> queryMap){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        queryWrapper.eq("user_type",ConstantUtils.CUSTOMER);
        queryWrapper.orderByDesc("create_time");
        IPage<User> iPage = new Page<>( Long.valueOf((String) queryMap.get("page")),Long.valueOf((String) queryMap.get("size")));
        if (!StringUtils.isEmpty(queryMap.get("name"))){
            //店员名字
            queryWrapper.like("name",queryMap.get("name"));
        }
        if (!StringUtils.isEmpty(queryMap.get("createTime"))){
            //注册时间
            queryWrapper.like("create_time",queryMap.get("createTime"));
        }
        if (!StringUtils.isEmpty(queryMap.get("phone"))){
            //手机号
            queryWrapper.like("phone",queryMap.get("phone"));
        }
        if (!StringUtils.isEmpty(queryMap.get("loginName"))){
            //登录名
            queryWrapper.like("login_name",queryMap.get("loginName"));
        }
        iPage = userService.page(iPage,queryWrapper);
        Map<String,Object> resultMap = new HashMap<>(8);
        resultMap.put("rows",iPage.getRecords());
        resultMap.put("pages",iPage.getPages());
        resultMap.put("total",iPage.getTotal());
        return ResultUtils.success("查询成功",resultMap);
    }

    /**
     * 上传用户头像
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResultVO uploadUserImg(MultipartFile file){
        File f = new File("D:/uploads");
        StringBuilder url = new StringBuilder();
        if (!f.exists()){
            f.mkdirs();
        }
        User user = new User();
        try {
            String fileName = file.getOriginalFilename();
            url.append(System.currentTimeMillis()).append(fileName);
            //新文件路径
            String resultUrl = "D:/uploads/"+ url;
            File upFile = new File(resultUrl);
            file.transferTo(upFile);
            String returnUrl = "http://localhost:9595/uploads/" + url;
            //修改用户头像
            User currUser = (User) SecurityUtils.getSubject().getPrincipal();
            user = userService.getById(currUser.getId());
            user.setUserImgUrl(returnUrl);
            userService.updateById(user);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtils.failed("上传失败");
        }
        //因为浏览器原因，设置虚拟路径为   /uploads/
        return ResultUtils.success("上传成功",user);
    }

}
