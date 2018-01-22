package com.uuabb.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by brander on 2017/12/22
 */
@Configuration //必须存在
@EnableSwagger2 //必须存在
@EnableWebMvc //必须存在
@ComponentScan(basePackages = {"com.uuabb.controller"}) //必须存在 扫描的API Controller package name 也可以直接扫描class (basePackageClasses)
public class WebAppConfig{
    @Bean
    public Docket customDocket() {
        //
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("王斑", "www.uuabb.cn", "yzbbanban@live.com");
        return new ApiInfo("Blog前台API接口",//大标题 title
                "Blog前台API接口",//小标题
                "0.0.1",//版本
                "www.uuabb.cn",//termsOfServiceUrl
                contact,//作者
                "Blog",//链接显示文字
                ""//网站链接
        );
    }
}