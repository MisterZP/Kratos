package com.kratos.config;

import com.kratos.entity.EsBean;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by zengping on 2016/12/7.
 */
@Configuration
@EnableConfigurationProperties(value = EsBean.class)
@EnableElasticsearchRepositories(basePackages = "co.paan.repository")
public class EsConfig {
    @Resource
    private EsBean esBean;

    @Bean
    public Client client() throws UnknownHostException {
        TransportClient client = TransportClient.builder().build();
        TransportAddress address = new InetSocketTransportAddress(InetAddress.getByName(esBean.getHost()), esBean.getPort());
        client.addTransportAddress(address);
        return client;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate(Client client) throws UnknownHostException {
        ElasticsearchTemplate est = new ElasticsearchTemplate(client);
        est.setSearchTimeout(esBean.getTimeOut());
        return est;
    }
}
