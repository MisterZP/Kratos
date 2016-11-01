package com.kratos.config;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by zengping on 2016/11/1.
 */
@Configuration
@EnableWebMvc
@EnableEurekaClient
@EnableHystrix
public class ApiConfig {
}
