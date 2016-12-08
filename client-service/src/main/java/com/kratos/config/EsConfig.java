package com.kratos.config;

import com.kratos.entity.EsBean;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Created by zengping on 2016/12/7.
 */
@Configuration
@EnableConfigurationProperties(value = EsBean.class)
@EnableElasticsearchRepositories(basePackages = "com.kratos.repository")
public class EsConfig {
    @Resource
    private EsBean esBean;

    @Bean
    public Client client() throws UnknownHostException {
        TransportClient client = TransportClient.builder()
                .settings(Settings.builder().put("cluster.name", esBean.getClusterName()).put("client.transport.sniff", esBean.isSniff())).build()
                .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(esBean.getHost(), esBean.getPort())));
        return client;
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(Client client) throws UnknownHostException {
        ElasticsearchTemplate est = new ElasticsearchTemplate(client);
        est.setSearchTimeout(esBean.getTimeOut());
        return est;
    }
}
