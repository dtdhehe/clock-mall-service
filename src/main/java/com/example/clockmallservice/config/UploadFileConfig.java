package com.example.clockmallservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/11/30 15:54
 * @description
 **/
@Configuration
public class UploadFileConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置虚拟上传路径，使SpringBoot可以读取本地磁盘文件
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:D:/uploads/");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
    }

    @Bean
    public HttpPutFormContentFilter httpPutFormContentFilter(){
        return new HttpPutFormContentFilter();
    }

}
