package com.kratos.model;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "druid.filter")
public class DruidFilterBean {
    private String filters;
    private String loginUsername;
    private String loginPassword;
    private String loginUrl;
    private String urlPatterns;
    private String exclusions;
}
