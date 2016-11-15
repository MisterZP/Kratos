package com.kratos.redis.shard;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisShardInfo;

import java.util.List;

/**
 * @author David on 16/3/31.
 */
public class ShardConfiguration {

    private GenericObjectPoolConfig pool;

    private List<JedisShardInfo> shards;

    public GenericObjectPoolConfig getPool() {
        return pool;
    }

    public void setPool(GenericObjectPoolConfig pool) {
        this.pool = pool;
    }

    public List<JedisShardInfo> getShards() {
        return shards;
    }

    public void setShards(List<JedisShardInfo> shards) {
        this.shards = shards;
    }

}
