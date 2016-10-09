package org.luna.txmqmsg.rocketmq.recover;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import com.alibaba.rocketmq.client.producer.MQProducer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.luna.txmqmsg.common.RetryRuleManager;
import org.luna.txmqmsg.rocketmq.common.LogPushManager;
import org.luna.txmqmsg.rocketmq.common.LogStorage;

/**
 * Created by luliru on 2016/9/18.
 */
public class TxLogRecovery {

    private String[] zookeeperAddressArray;

    private String leaderSelectorPath = "luna_tx/recover_leader";

    private int executeThreadNum = 2;

    private DataSource dataSource;

    private String databaseName;

    private ExecutorService pushExecutor;

    private CuratorFramework client;

    private LeaderLatch leaderLatch;

    private MQProducer mqProducer;

    private ScheduledExecutorService fetchTashExecutor;

    public void init(){
        try{
            pushExecutor = Executors.newFixedThreadPool(executeThreadNum);
            LogStorage txLogRetryStorage = new LogStorage(dataSource,databaseName);

            RetryRuleManager retryRuleManager = new RetryRuleManager();
            LogPushManager logPushManager = new LogPushManager(txLogRetryStorage,retryRuleManager,mqProducer);

            LogRecoverTask fetchTask = new LogRecoverTask(txLogRetryStorage,logPushManager,pushExecutor);

            String conectionZookeeperAddress = zookeeperAddressArray[new Random().nextInt(zookeeperAddressArray.length)];

            ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.newClient(conectionZookeeperAddress, retryPolicy);
            client.start();

            leaderLatch = new LeaderLatch(client,leaderSelectorPath);
            leaderLatch.addListener(new RecoverLeaderLatchListener(fetchTask));
            leaderLatch.start();

            fetchTashExecutor = Executors.newScheduledThreadPool(1);
            fetchTashExecutor.scheduleAtFixedRate(fetchTask,0,1,TimeUnit.SECONDS);  //每隔一秒执行一次查询
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void destroy(){
        try{
            pushExecutor.shutdown();
            client.close();
            leaderLatch.close();
            fetchTashExecutor.shutdown();
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

    public void setMqProducer(MQProducer mqProducer) {
        this.mqProducer = mqProducer;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}

