package org.luna.common.utils.lock.redis.concurrent;

import org.luna.common.utils.lock.redis.config.RedisDistributedLockConfig;
import org.luna.common.utils.lock.redis.config.RedisInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by luliru on 2016/6/15.
 */
public class TryLockTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(TryLockTask.class);

    private RedisDistributedLockConfig config;

    private RedisInstance redisInstance;

    private AtomicInteger lockSuccessNum;

    private CountDownLatch latch;

    private String lockValue;

    public TryLockTask(RedisDistributedLockConfig config, AtomicInteger lockSuccessNum, CountDownLatch latch, RedisInstance redisInstance, String lockValue){
        this.config = config;
        this.lockSuccessNum = lockSuccessNum;
        this.latch = latch;
        this.redisInstance = redisInstance;
        this.lockValue = lockValue;
    }

    public void run() {
        Jedis jedis = null;
        try{
            JedisPool poll = redisInstance.getPool();
            jedis = poll.getResource();
            jedis.select(redisInstance.getDbIndex());
            String lockResult = jedis.set(config.getLockName(),lockValue,"NX","PX",config.getLockoutMillisecond());
            if("OK".equals(lockResult)){
                lockSuccessNum.incrementAndGet();
            }
        }catch (Exception e){
            log.error("连接redis实例失败",e);
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        latch.countDown();
    }
}
