package org.luna.txmqmsg.rocketmq.recover;

/**
 * Created by luliru on 2016/9/19.
 */
public class TxLogRetryFetchTask implements Runnable {

    private TxLogRetryStorage txLogRetryStorage;

    private boolean isWorking;

    public TxLogRetryFetchTask(TxLogRetryStorage txLogRetryStorage){
        this.txLogRetryStorage = txLogRetryStorage;
    }

    @Override
    public void run() {

    }

    public void setWorking(boolean working) {
        isWorking = working;
    }
}
