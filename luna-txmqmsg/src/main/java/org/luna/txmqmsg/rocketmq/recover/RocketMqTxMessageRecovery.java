package org.luna.txmqmsg.rocketmq.recover;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by luliru on 2016/9/18.
 */
public class RocketMqTxMessageRecovery {

    private String[] zookeeperAddressArray;

    private String leaderSelectorPath = "luna_tx/recover_leader";

    private int executeThreadNum = 2;

    private DataSource dataSource;

    private String databaseName;

    private TxLogRetryStorage txLogRetryStorage;

    private ExecutorService executor;

    private Map<Integer,RetryInterval> retryRules;

    private CuratorFramework client;

    private LeaderLatch leaderLatch;

    public void init(){
        try{
            executor = Executors.newFixedThreadPool(executeThreadNum);
            txLogRetryStorage = new TxLogRetryStorage(dataSource,databaseName);

            TxLogRetryFetchTask fetchTask = new TxLogRetryFetchTask(txLogRetryStorage);

            String conectionZookeeperAddress = zookeeperAddressArray[new Random().nextInt(zookeeperAddressArray.length)];

            ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.newClient(conectionZookeeperAddress, retryPolicy);
            client.start();

            leaderLatch = new LeaderLatch(client,leaderSelectorPath);
            leaderLatch.addListener(new RecoverLeaderLatchListener(fetchTask));
            leaderLatch.start();

            retryRules = new HashMap<>();
            retryRules.put(1,new RetryInterval(3,TimeUnit.MINUTES));
            retryRules.put(2,new RetryInterval(8,TimeUnit.MINUTES));
            retryRules.put(3,new RetryInterval(20,TimeUnit.MINUTES));
            retryRules.put(4,new RetryInterval(1,TimeUnit.HOURS));
            retryRules.put(5,new RetryInterval(3,TimeUnit.HOURS));
            retryRules.put(6,new RetryInterval(8,TimeUnit.HOURS));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void destroy(){
        try{
            executor.shutdown();
            client.close();
            leaderLatch.close();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void setZookeeperAddresses(String zookeeperAddresses) {
        zookeeperAddressArray = zookeeperAddresses.split(",");
    }

    public void setLeaderSelectorPath(String leaderSelectorPath) {
        this.leaderSelectorPath = leaderSelectorPath;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}

class RetryInterval{

    private Integer interval;
    private TimeUnit timeUnit;

    public RetryInterval(Integer interval,TimeUnit timeUnit){
        this.interval = interval;
        this.timeUnit = timeUnit;
    }
}

