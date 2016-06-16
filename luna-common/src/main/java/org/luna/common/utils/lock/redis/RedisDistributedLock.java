package org.luna.common.utils.lock.redis;

import org.luna.common.utils.lock.redis.concurrent.TryLockTask;
import org.luna.common.utils.lock.redis.concurrent.UnlockTask;
import org.luna.common.utils.lock.redis.config.RedisDistributedLockConfig;
import org.luna.common.utils.lock.redis.config.RedisInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by luliru on 2016/6/12.
 */
public class RedisDistributedLock implements Lock {

    private static final Logger log = LoggerFactory.getLogger(RedisDistributedLock.class);

    private RedisDistributedLockConfig config;

    private String lastLockValue;

    public RedisDistributedLock(RedisDistributedLockConfig config){
        this.config = config;
        if(config.getExecutor() == null){
            ExecutorService executor = Executors.newFixedThreadPool(config.getRedisList().size());
            config.setExecutor(executor);
        }
    }


    public void lock() {
        while(true){
            if(tryLock() == true){
                break;
            }
            try {
                long sleepMillisecond = config.getLockoutMillisecond() + (new Random().nextInt(new Long(config.getLockoutMillisecond()).intValue()) );
                Thread.sleep(sleepMillisecond);
            } catch (InterruptedException e) {
                log.error("trylock fail and sleep interrupted",e);
            }//获取锁失败时，让线程随机睡眠一段时间
        }
    }

    public void lockInterruptibly() throws InterruptedException {
        throw new RuntimeException("unsupport method");
    }

    public boolean tryLock() {
        boolean lockSuccess = false;
        Long nanoTime = System.nanoTime();
        String lockValue = nanoTime.toString();
        List<RedisInstance> redisList = config.getRedisList();
        int instanceNum = redisList.size();

        AtomicInteger lockSuccessNum = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(instanceNum);
        for(RedisInstance redis:redisList){
            TryLockTask tryLockTask = new TryLockTask(config,lockSuccessNum,latch,redis,lockValue);
            config.getExecutor().execute(tryLockTask);
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("向redis获取锁时被打断",e);
        }
        //超过半数则认为锁定成功
        //如果有这个锁抢占比较激烈的话，可能会出现每个节点都抢占部分的redis节点，最后都失败
        if(lockSuccessNum.intValue() * 2 > instanceNum){
            lastLockValue = lockValue;
            lockSuccess = true;
        }else{
            tryUnlock(lockValue);
        }
        return lockSuccess;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long timeoutTime = System.currentTimeMillis()+ TimeUnit.MILLISECONDS.convert(time, unit);

        while(true){
            boolean trySuccess = tryLock();
            if(trySuccess){
                return true;
            }else if(System.currentTimeMillis() >= timeoutTime){
                return false;
            }
        }
    }

    public void unlock() {
        tryUnlock(lastLockValue);
    }

    private void tryUnlock(String lockValue){
        List<RedisInstance> redisList = config.getRedisList();
        for(RedisInstance redis:redisList){
            UnlockTask unlockTask = new UnlockTask(config,redis,lockValue);
            config.getExecutor().execute(unlockTask);
        }
    }

    public Condition newCondition() {
        throw new RuntimeException("unsupport method");
    }

    public void shutdown(){
        config.getExecutor().shutdown();
    }

}
