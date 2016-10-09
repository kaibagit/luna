package org.luna.txmqmsg.rocketmq.recover;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;

import org.luna.txmqmsg.rocketmq.common.LogPushManager;
import org.luna.txmqmsg.rocketmq.common.LogStorage;
import org.luna.txmqmsg.rocketmq.common.PersistentLog;

/**
 * Created by luliru on 2016/9/19.
 */
public class LogRecoverTask extends TimerTask implements Runnable {

    private LogStorage logStorage;

    private LogPushManager logPushManager;

    private ExecutorService pushExecutor;

    private boolean isWorking;

    private volatile boolean isRunning = false;

    public LogRecoverTask(LogStorage logStorage, LogPushManager logPushManager,ExecutorService pushExecutor){
        this.logStorage = logStorage;
        this.logPushManager = logPushManager;
        this.pushExecutor = pushExecutor;
    }

    @Override
    public void run() {
        if(isWorking && !isRunning){
            isRunning = true;
            List<PersistentLog> messageList = logStorage.findReadyToRetryMessages();
            for(final PersistentLog txMessage : messageList){
                pushExecutor.execute(new Runnable(){
                    @Override
                    public void run() {
                        logPushManager.pushToMq(txMessage);
                    }
                });
            }
            isRunning = false;
        }
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }
}
