package com.kratos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by zengping on 2016/11/1.
 */
@Configuration
@EnableWebMvc
@EnableEurekaClient
@EnableHystrix
public class ApiConfig {

    @Autowired
    private Environment environment;

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        //resolver.setResolveLazily(true);// resolveLazily属性启用是为了推迟文件解析
        //resolver.setMaxInMemorySize(40960);
        //resolver.setMaxUploadSize(2 * 1024 * 1024);// 上传文件大小 2M 50*1024*1024
        return resolver;
    }
}
