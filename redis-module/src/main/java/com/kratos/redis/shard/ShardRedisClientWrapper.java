package com.kratos.redis.shard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author David on 16/3/31.
 */
public class ShardRedisClientWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShardRedisClientWrapper.class);

    private Environment environment;

    private static ShardedJedisPool shardedJedisPool = null;

    private static class ShardRedisClientWrapperHolder{
        private static ShardRedisClientWrapper INSTANCE = new ShardRedisClientWrapper();
    }

    public static ShardRedisClientWrapper getInstance() {
        return ShardRedisClientWrapperHolder.INSTANCE;
    }

    private ShardRedisClientWrapper(){

    }

    public void loadRedisConfig(Environment environment) {
        ShardConfiguration configuration = ShardRedisConfigurationBuilder.getInstance().setEnviroment(environment).loadConfiguration();
        synchronized (ShardRedisClientWrapper.class) {
            if(null == shardedJedisPool) {
                shardedJedisPool = new ShardedJedisPool(
                        configuration.getPool(),
                        configuration.getShards());
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Running new redis client using : {}" + configuration);
            }

        }
    }

    public ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }

    public ShardedJedis getResource(ShardRedisCallback callback) {
        return shardedJedisPool.getResource();
    }


}
