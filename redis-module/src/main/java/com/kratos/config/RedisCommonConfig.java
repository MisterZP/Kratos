package com.kratos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import com.kratos.redis.shard.CacheClient;

/**
 * Created by zengping on 2016/11/14.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class RedisCommonConfig {

    @Autowired
    private Environment environment;

    @Bean
    public CacheClient getCacheClient(){
        environment.getProperty("shards.1.db");
        CacheClient cacheClient = new CacheClient();
        cacheClient.loadShardedJedisPool(environment);
        return cacheClient;
    }

}
