package org.luna.common.utils.lock.redis.config;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by luliru on 2016/6/15.
 */
public class RedisDistributedLockConfig {

    private List<RedisInstance> redisList;

    private String lockName = "RedisDistributedLock";

    private long lockoutMillisecond = 3000;

    private ExecutorService executor;

    public List<RedisInstance> getRedisList() {
        return redisList;
    }

    public void setRedisList(List<RedisInstance> redisList) {
        this.redisList = redisList;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public long getLockoutMillisecond() {
        return lockoutMillisecond;
    }

    public void setLockoutMillisecond(long lockoutMillisecond) {
        this.lockoutMillisecond = lockoutMillisecond;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }
}
