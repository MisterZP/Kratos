package com.kratos.redis.shard;

import com.kratos.redis.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import redis.clients.jedis.ShardedJedis;

/**
 * @author David on 16/4/5.
 */
public class CacheClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheClient.class);
    private ShardRedisClientWrapper shardRedisClientWrapper = ShardRedisClientWrapper.getInstance();

    public void loadShardedJedisPool(Environment environment){
        shardRedisClientWrapper.loadRedisConfig(environment);
    }
    @SuppressWarnings("unchecked")
    public <T> T get(final String key) {
        return (T) execute(new ShardRedisCallback() {
            @Override
            public Object doWithRedis(ShardedJedis shardedJedis) {
                return SerializeUtil.unserialize(shardedJedis.get(key.getBytes()));
            }
        });
    }

    /**
     *
     * @param key 键
     * @param value 值
     */
    public <T> void set(final String key, final T value) {
        execute(new ShardRedisCallback() {
            @Override
            public Object doWithRedis(ShardedJedis shardedJedis) {
                shardedJedis.set(key.getBytes(), SerializeUtil.serialize(value));
                return null;
            }
        });
    }

    /**
     * 设置时指定过期时间 <br />
     * <b>支持Redis server 2.6.12 以后的版本。当缓存中已经存在key时不会生效</b>
     * @param key 键
     * @param value 值
     * @param expInSecond
     *            过期时间, 单位: 秒
     */
    public <T> void set(final String key, final T value, final int expInSecond) {
        set(key, value, expInSecond, get(key) != null);
    }

    /**
     * 设置时指定过期时间 <br />
     * <b>支持Redis server 2.6.12 以后的版本。当缓存中已经存在key时不会生效</b>
     * @param key 键
     * @param value 值
     * @param expInSecond
     *            过期时间, 单位: 秒
     * @param keyExist key是否还存在redis缓存中
     */
    public <T> void set(final String key, final T value, final int expInSecond, final boolean keyExist) {
        execute(new ShardRedisCallback() {
            @Override
            public Object doWithRedis(ShardedJedis shardedJedis) {
                shardedJedis.set(key.getBytes(), SerializeUtil.serialize(value),
                        (keyExist ? "XX" :"NX").getBytes(), "EX".getBytes(), expInSecond);
                return null;
            }
        });
    }


    /**
     * @param key 键
     */
    public void delete(final String key) {
        execute(new ShardRedisCallback() {
            @Override
            public Object doWithRedis(ShardedJedis shardedJedis) {
                shardedJedis.del(key);
                return null;
            }
        });
    }

    /**
     * @param keys 多个键
     */
    public void delete(String[] keys) {
        if (keys == null || keys.length == 0) {
            LOGGER.error("The keys of the object to be removed is null.");
            return;
        }
        for (String key : keys) {
            delete(key);
        }
    }

    /**
     * 原子自增
     * @param key 键
     * @return 增长后的value
     */
    public Long incr(final String key) {
        return execute(new ShardRedisCallback<Long>() {
            @Override
            public Long doWithRedis(ShardedJedis shardedJedis) {
                return shardedJedis.incr(key);
            }
        });
    }
    /**
     * 为一个key设置失效时间
     * @param key 键
     * @param expireSecond 失效时间 秒
     * @return 增长后的value
     */
    public Long expire(final String key, final int expireSecond) {
        return execute(new ShardRedisCallback<Long>() {
            @Override
            public Long doWithRedis(ShardedJedis shardedJedis) {
                return shardedJedis.expire(key, expireSecond);
            }
        });
    }

    /**
     * 通用回调，适用于高级接口
     * @param callback 回调函数
     * @return 缓存中的对象
     */
    public <T> T execute(ShardRedisCallback<T> callback) {
        try (ShardedJedis shardJedis = shardRedisClientWrapper.getResource(callback)) {
            return callback.doWithRedis(shardJedis);
        }
    }

}
