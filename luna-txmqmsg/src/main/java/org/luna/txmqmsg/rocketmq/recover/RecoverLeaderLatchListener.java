package org.luna.txmqmsg.rocketmq.recover;

import org.apache.curator.framework.recipes.leader.LeaderLatchListener;

/**
 * Created by luliru on 2016/9/29.
 */
public class RecoverLeaderLatchListener implements LeaderLatchListener {

    private TxLogRetryFetchTask txLogRetryFetchTask;

    public RecoverLeaderLatchListener(TxLogRetryFetchTask txLogRetryFetchTask){
        this.txLogRetryFetchTask = txLogRetryFetchTask;
    }

    @Override
    public void isLeader() {
        txLogRetryFetchTask.setWorking(true);
    }

    @Override
    public void notLeader() {
        txLogRetryFetchTask.setWorking(false);
    }
}
