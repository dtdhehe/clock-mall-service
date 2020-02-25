package com.example.clockmallservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2020/2/15 16:07
 * @description
 **/
@Controller
@RequestMapping("")
public class IndexController {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

}
