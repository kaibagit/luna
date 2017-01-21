package org.luna.rpc.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.luna.rpc.core.Lifecycle;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.core.URL;
import org.luna.rpc.registry.Registry;
import org.luna.rpc.util.LoggerUtil;

/**
 * Zookeeper注册中心
 * Created by luliru on 2016/12/30.
 */
public class ZookeeperRegistry implements Registry,Lifecycle {

    private URL url;

    private CuratorFramework client;

    public ZookeeperRegistry(URL url,CuratorFramework client){
        this.url = url;
        this.client = client;
    }

    @Override
    public void start() {
        client.start();
    }

    @Override
    public void destory() {
        CloseableUtils.closeQuietly(client);
    }

    @Override
    public void register(URL url) {
        try{
            String parentPath = String.format("/luna/%s/%s/providers",url.getGroup(),url.getService());
            String nodePath = String.format("%s/%s:%d",parentPath,url.getHost(),url.getPort());
            if(client.checkExists().forPath(parentPath) == null){
                client.create().creatingParentsIfNeeded().forPath(parentPath);
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(nodePath, url.toFullStr().getBytes());
            LoggerUtil.info(String.format("Register %s to zookeeper(%s) success.",url,this.url));
        }catch (Exception e){
            throw new LunaRpcException(String.format("Failed to register %s to zookeeper(%s), cause: %s", url, this.url, e.getMessage()), e);
        }
    }
}
