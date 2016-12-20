package com.kratos.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by zengping on 2016/12/19.
 */
@Configuration
@EnableWebMvc
@EnableDiscoveryClient
@EnableHystrix
@EnableRedisHttpSession
public class TestConfig {
}
