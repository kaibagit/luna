package org.luna.txmqmsg.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 重试规则管理器
 * Created by luliru on 2016/10/8.
 */
public class RetryRuleManager {

    private Map<Integer,RetryInterval> retryRules;

    public RetryRuleManager(){
        retryRules = new HashMap<>();
        retryRules.put(1,new RetryInterval(3,TimeUnit.MINUTES));
        retryRules.put(2,new RetryInterval(8,TimeUnit.MINUTES));
        retryRules.put(3,new RetryInterval(20,TimeUnit.MINUTES));
        retryRules.put(4,new RetryInterval(1,TimeUnit.HOURS));
        retryRules.put(5,new RetryInterval(3,TimeUnit.HOURS));
        retryRules.put(6,new RetryInterval(8,TimeUnit.HOURS));
    }

    public RetryInterval getNext(int currentTimes){
        return retryRules.get(currentTimes+1);
    }

    public static class RetryInterval{

        private Integer interval;
        private TimeUnit timeUnit;

        public RetryInterval(Integer interval,TimeUnit timeUnit){
            this.interval = interval;
            this.timeUnit = timeUnit;
        }

        public long toMillis(){
            return timeUnit.toMillis(interval);
        }
    }
}

