package com.kratos.redis.shard;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.*;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;

import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author David on 16/3/31.
 */
public class ShardRedisConfigurationBuilder {
    private Environment environment;

    private static final Logger LOGGER = LoggerFactory.getLogger(ShardRedisConfigurationBuilder.class);
    private static final String SHARD_PROPERTY_CONFIG_PATTERN = "^" + Pattern.quote("shards") + "\\.\\d+\\..*";

    private static class ShardRedisConfigurationBuilderHolder{
        private static ShardRedisConfigurationBuilder INSTANCE = new ShardRedisConfigurationBuilder();
    }

    private ShardRedisConfigurationBuilder(){

    }

    public ShardRedisConfigurationBuilder setEnviroment(Environment environment){
        this.environment = environment;
        return this;
    }

    public Environment getEnviroment(Environment environment){
        return environment;
    }

    public static ShardRedisConfigurationBuilder getInstance() {
        return ShardRedisConfigurationBuilderHolder.INSTANCE;
    }

    public ShardConfiguration loadConfiguration() {
        LOGGER.info("load redis properties");
        return loadBySpringEnviroment();
    }

    private ShardConfiguration loadBySpringEnviroment() {
        if(null == environment) {
            throw new RuntimeException("could not find config enviroment");
        }
        return propertyLoader(environment);
    }

    private ShardConfiguration propertyLoader(Environment environment) {
        try {
            ShardConfiguration shardConfiguration = new ShardConfiguration();
            String maxIdle = environment.getProperty("pool.maxIdle");
            String maxTotal = environment.getProperty("pool.maxTotal");
            String minIdle = environment.getProperty("pool.minIdle");
            String testOnBorrow = environment.getProperty("pool.testOnBorrow");
            int lastShardIndex = 0;
            List<Map<String, String>> shardsMapList = new ArrayList<>();
            Iterator<PropertySource<?>> itePro;
            Map<String, Object> propertiesMap = new HashMap<>();
            for(itePro = ((StandardEnvironment)environment).getPropertySources().iterator(); itePro.hasNext(); ){
                PropertySource<?> proSourceParent = itePro.next();
                if(proSourceParent instanceof CompositePropertySource){
                    if(!"bootstrapProperties".equals(proSourceParent.getName()))
                        continue;
                    Collection<PropertySource<?>> colleProParent = (((CompositePropertySource) proSourceParent)).getPropertySources();
                    if(CollectionUtils.isEmpty(colleProParent))
                        continue;
                    for (PropertySource proSourChild : colleProParent){
                        if(!"configService".equals(proSourChild.getName()))
                            continue;
                        if(proSourChild instanceof CompositePropertySource){
                            CompositePropertySource targetPropertySource = (CompositePropertySource) proSourChild;
                            Collection<PropertySource<?>> targetPropMap = targetPropertySource.getPropertySources();
                            if(CollectionUtils.isEmpty(targetPropMap))
                                continue;
                            for (PropertySource proMap : targetPropMap) {
                                if(proMap instanceof MapPropertySource){
                                    propertiesMap.putAll(((MapPropertySource) proMap).getSource());
                                }
                            }
                        }
                    }
                }
            }
            Set<String> keySet = propertiesMap.keySet();
            for (String key : keySet) {
                Object v = propertiesMap.get(key);
                String value = null != v ? v.toString() : null;
                if (key.matches(SHARD_PROPERTY_CONFIG_PATTERN)) {
                    Integer shardNum = Utils.findInt(key);
                    if(null == shardNum) {
                        throw new RuntimeException("shard config pattern error. text:" + key);
                    }
                    if(lastShardIndex != shardNum) {
                        lastShardIndex = shardNum;
                    }
                    getCurrentRow(shardsMapList, lastShardIndex - 1).put(coverToSingleCommonKey(key), value);
                }
            }
            shardConfiguration.setPool(createPoolConfig(minIdle, maxIdle, maxTotal,
                    testOnBorrow!=null && "1".equals(testOnBorrow)));
            shardConfiguration.setShards(createShardsInfo(shardsMapList));
            return shardConfiguration;
        } catch (Exception e) {
            LOGGER.error("property load error!", e);
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> getCurrentRow(List<Map<String, String>> shardsMapList, int currentIndex) {
        if(currentIndex >= shardsMapList.size()) {
            Map<String, String> row = new HashMap<>();
            shardsMapList.add(row);
            return row;
        }else {
            return shardsMapList.get(currentIndex);
        }
    }

    private String coverToSingleCommonKey(String key) {
        String[] split = key.split("\\.");
        if(split.length < 3) {
            throw new IllegalArgumentException("key [" + key + "] not matches the pattern : [shard.{num}.xxx]");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 2 ; i < split.length; i++) {
            stringBuilder.append(split[i]);
        }
        return stringBuilder.toString();
    }

    private List<JedisShardInfo> createShardsInfo(List<Map<String, String>> shardsMap) {
        List<JedisShardInfo> shardsInfo = new ArrayList<>();
        try {
            for(Map<String, String> info : shardsMap) {
                String host = info.get("host") == null ? "localhost" : info.get("host");
                int port = info.get("port") == null ? Protocol.DEFAULT_PORT : Integer.parseInt(info.get("port"));
                String password = info.get("password"),
                        db = info.get("db"),
                        connectionTimeout = info.get("connectionTimeout"),
                        soTimeout = info.get("soTimeout");
                URI redisURI = new URI("redis",
                        (isNull(password) ? null : "foo:" + password),
                        host,
                        port,
                        "/" + (isNull(db) ? 0 : db), null, null);
                JedisShardInfo jedisInfo = new JedisShardInfo(redisURI);
                if(!isNull(connectionTimeout)) {
                    jedisInfo.setConnectionTimeout(Integer.parseInt(connectionTimeout));
                }
                if(!isNull(soTimeout)) {
                    jedisInfo.setSoTimeout(Integer.parseInt(soTimeout));
                }
                shardsInfo.add(jedisInfo);
            }
        }catch (Exception e) {
            throw new IllegalArgumentException("shard info arguments pattern error.");
        }
        return shardsInfo;
    }

    private GenericObjectPoolConfig createPoolConfig(String minIdle
            , String maxIdle
            , String maxTotal, boolean testOnBorrow

    ) {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        if(null != maxIdle) {
            poolConfig.setMaxIdle(Integer.parseInt(maxIdle));
        }
        if(null != maxTotal) {
            poolConfig.setMaxTotal(Integer.parseInt(maxTotal));
        }
        if(null != minIdle) {
            poolConfig.setMinIdle(Integer.parseInt(minIdle));
        }
        poolConfig.setTestOnBorrow(testOnBorrow);
        return poolConfig;
    }


    private boolean isNull(String text) {
        return text == null || "".equals(text);
    }

}
