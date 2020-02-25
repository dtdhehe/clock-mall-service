package com.example.clockmallservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.clockmallservice.entity.Address;
import com.example.clockmallservice.entity.User;
import com.example.clockmallservice.service.AddressService;
import com.example.clockmallservice.util.ConstantUtils;
import com.example.clockmallservice.util.ResultUtils;
import com.example.clockmallservice.vo.ResultVO;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/12/13 11:15
 * @description
 **/
@Api(tags = "收货地址")
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 新增收货地址
     * @param address
     * @return
     */
    @PostMapping("")
    public ResultVO addAddress(@RequestBody Address address){
        if (StringUtils.isEmpty(address.getCustomerId())){
            //未传收货人id，默认为当前登录用户id
            User currUser = (User)SecurityUtils.getSubject().getPrincipal();
            address.setCustomerId(currUser.getId());
        }
        //查询当前用户的收货地址
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        queryWrapper.eq("customer_id",address.getCustomerId());
        List<Address> addressList = addressService.list(queryWrapper);
        address.setIsDefault(ConstantUtils.NOTACTIVE);
        if (addressList.size() == 0){
            //首次保存地址，默认为默认地址
            address.setIsDefault(ConstantUtils.ACTIVE);
        }
        if (addressList.size() == 5){
            return ResultUtils.failed("收货地址最多保存5个");
        }
        return addressService.save(address)?ResultUtils.success("新增收货地址成功"):ResultUtils.failed("新增失败");
    }

    /**
     * 修改收货地址
     * @param address
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResultVO updateAddress(@RequestBody Address address,@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        if (StringUtils.isEmpty(address.getId())){
            address.setId(id);
        }
        return addressService.updateById(address)?ResultUtils.success("修改收货地址信息成功"):ResultUtils.failed("修改失败");
    }

    /**
     * 选择默认地址
     * @param id
     * @return
     */
    @PutMapping("/default/{id}")
    public ResultVO checkAddress(@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        //先查询所有的收货地址
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        User currUser = (User)SecurityUtils.getSubject().getPrincipal();
        queryWrapper.eq("customer_id",currUser.getId());
        List<Address> addressList = addressService.list(queryWrapper);
        //循环把所有地址设为非默认
        for (Address item : addressList){
            item.setIsDefault(ConstantUtils.NOTACTIVE);
            addressService.updateById(item);
        }
        //再把所选地址设为默认
        Address address = addressService.getById(id);
        address.setIsDefault(ConstantUtils.ACTIVE);
        return addressService.updateById(address)?ResultUtils.success("设置默认地址成功"):ResultUtils.failed("设置失败");
    }

    /**
     * 删除收货地址
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVO deleteAddress(@PathVariable String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        Address address = addressService.getById(id);
        address.setValidFlag(ConstantUtils.NOTACTIVE);
        return addressService.updateById(address)?ResultUtils.success("删除收货地址成功"):ResultUtils.failed("删除失败");

    }

    /**
     * 根据id获得收货地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultVO getAddress(@PathVariable("id") String id){
        if (StringUtils.isEmpty(id)){
            return ResultUtils.failed("传入的id不能为空");
        }
        Address address = addressService.getById(id);
        return ResultUtils.success("查询成功",address);
    }

    /**
     * 查询所有收货地址
     * @return
     */
    @GetMapping("/list")
    public ResultVO queryAddressList(){
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("valid_flag",ConstantUtils.ACTIVE);
        User currUser = (User)SecurityUtils.getSubject().getPrincipal();
        queryWrapper.eq("customer_id",currUser.getId());
        queryWrapper.orderByDesc("is_default");
        queryWrapper.orderByAsc("create_time");
        List<Address> addressList = addressService.list(queryWrapper);
        return ResultUtils.success("查询成功",addressList);
    }

}
