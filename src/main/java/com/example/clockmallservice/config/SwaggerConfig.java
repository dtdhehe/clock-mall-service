package com.example.clockmallservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 包豪娟
 * @version 1.0.0
 * @date 2019/11/5 15:15
 * @description
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createApi(){
        //Docket是Swagger重要的构造器，为swagger配置提供默认值和方法
        //DocumentationType.SWAGGER_2表示使用swagger版本2.0
        return new Docket(DocumentationType.SWAGGER_2)
                //传入自定义api描述
                .apiInfo(apiInfo())
                //返回一个api选择构建器
                .select()
                //指定扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.example.store.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 生成一个包含自定义信息的ApiInfo类
     * @return ApiInfo类
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("接口文档")
                .description("便利店接口文档")
                //指定访问的路径
                .termsOfServiceUrl("http://localhost:9090/")
                //添加联系人信息
                .contact(new Contact("dtdhehe","URL","dtdhehe@sina.com"))
                //版本描述
                .version("1.0")
                .build();
    }
}
