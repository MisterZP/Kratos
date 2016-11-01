package com.kratos.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zengping on 2016/10/25.
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "druid.dataSource")
public class DruidBean {
    //@Value("${jdbc.url}")
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int initialSize;
    private int minIdle;
    private int maxActive;
    private long maxWait;
    private long minEvictableIdleTimeMillis;
    private long timeBetweenEvictionRunsMillis;
    private String validationQuery;
    private boolean testWhileIdle;
    private String filters;
}
