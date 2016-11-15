package com.kratos.redis.shard;

import com.kratos.redis.DummyReadWriteLock;
import org.apache.ibatis.cache.Cache;
import com.kratos.redis.SerializeUtil;
import redis.clients.jedis.ShardedJedis;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author David on 16/3/30.
 */
public final class ShardRedisCache implements Cache {
    private final ReadWriteLock readWriteLock = new DummyReadWriteLock();

    private String id;

    private static final ShardRedisClientWrapper redisClient = ShardRedisClientWrapper.getInstance();

    public ShardRedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    private Object execute(ShardRedisCallback callback) {
        try (ShardedJedis shardJedis = redisClient.getShardedJedisPool().getResource()) {
            return callback.doWithRedis(shardJedis);
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getSize() {
        return (Integer) execute(new ShardRedisCallback() {
            @Override
            public Object doWithRedis(ShardedJedis shardedJedis) {
                Map<byte[], byte[]> result = shardedJedis.hgetAll(id.getBytes());
                return result.size();
            }
        });
    }

    @Override
    public void putObject(final Object key, final Object value) {
        execute(new ShardRedisCallback() {
            @Override
            public Object doWithRedis(ShardedJedis shardedJedis) {
                shardedJedis.hset(id.getBytes(), key.toString().getBytes(), SerializeUtil.serialize(value));
                return null;
            }
        });
    }

    @Override
    public Object getObject(final Object key) {
        return execute(new ShardRedisCallback() {
            @Override
            public Object doWithRedis(ShardedJedis shardedJedis) {
                return SerializeUtil.unserialize(shardedJedis.hget(id.getBytes(), key.toString().getBytes()));
            }
        });
    }

    @Override
    public Object removeObject(final Object key) {
        return execute(new ShardRedisCallback() {
            @Override
            public Object doWithRedis(ShardedJedis shardedJedis) {
                return shardedJedis.hdel(id, key.toString());
            }
        });
    }

    @Override
    public void clear() {
        execute(new ShardRedisCallback() {
            @Override
            public Object doWithRedis(ShardedJedis shardedJedis) {
                shardedJedis.del(id);
                return null;
            }
        });

    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    @Override
    public String toString() {
        return "Redis {" + id + "}";
    }
}
