package org.luna.common.utils.lock.redis.config;

import redis.clients.jedis.JedisPool;

/**
 * Created by luliru on 2016/6/15.
 */
public class RedisInstance {

    private JedisPool pool;

    private int dbIndex;

    public RedisInstance(JedisPool pool){
        this.pool = pool;
    }

    public RedisInstance(JedisPool pool,int dbIndex){
        this.pool = pool;
        this.dbIndex = dbIndex;
    }

    public JedisPool getPool() {
        return pool;
    }

    public void setPool(JedisPool pool) {
        this.pool = pool;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }
}
