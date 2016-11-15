package com.kratos.redis.shard;

import redis.clients.jedis.*;

import java.util.List;

/**
 * @author David on 16/7/22.
 */
public class SharededJedisWrapper extends ShardedJedis{

    public SharededJedisWrapper(List<JedisShardInfo> shards) {
        super(shards);
    }

    @Override
    public JedisShardInfo getShardInfo(byte[] key) {
        JedisShardInfo shardInfo = super.getShardInfo(key);
        return shardInfo;
    }
}
