package com.kratos.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zengping on 2016/12/7.
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "elasticsearch.dataSource")
public class EsBean {
    private String host;
    private int port;
    private String timeOut;
    private boolean sniff;
    private String clusterName;
}
