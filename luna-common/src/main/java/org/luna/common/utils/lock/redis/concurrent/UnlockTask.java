package org.luna.common.utils.lock.redis.concurrent;

import org.apache.commons.codec.digest.DigestUtils;
import org.luna.common.utils.lock.redis.config.RedisDistributedLockConfig;
import org.luna.common.utils.lock.redis.config.RedisInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by luliru on 2016/6/15.
 */
public class UnlockTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(UnlockTask.class);

    private RedisDistributedLockConfig config;

    private RedisInstance redisInstance;

    private String lastLockValue;

    public UnlockTask(RedisDistributedLockConfig config, RedisInstance redisInstance, String lastLockValue){
        this.config = config;
        this.redisInstance = redisInstance;
        this.lastLockValue = lastLockValue;
    }

    public void run() {
        Jedis jedis = null;
        try{
            JedisPool pool = redisInstance.getPool();
            jedis = pool.getResource();
            try{
                jedis.evalsha(sha(getLuaScript()),1,config.getLockName(),lastLockValue);
            }catch (Exception e){
                log.info("no unlock lua script",e);
                jedis.eval(getLuaScript(),1,config.getLockName(),lastLockValue);
            }

        }catch (Exception e){
            log.error("unlock from redis error",e);
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    private String getLuaScript(){
        return "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";
    }

    private String sha(String str){
        return DigestUtils.shaHex(str);
    }
}
