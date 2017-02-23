package org.luna.rpc.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.luna.rpc.common.constant.Constraint;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.Lifecycle;
import org.luna.rpc.core.exception.LunaRpcException;
import org.luna.rpc.core.URL;
import org.luna.rpc.registry.NotifyListener;
import org.luna.rpc.registry.Registry;
import org.luna.rpc.registry.RegistryURL;
import org.luna.rpc.util.LoggerUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Zookeeper注册中心
 * Created by luliru on 2016/12/30.
 */
public class ZookeeperRegistry implements Registry,Lifecycle {

    private RegistryURL url;

    private CuratorFramework client;

    private Map<String,ZookeeperWatcherManager> watcherManagers = new ConcurrentHashMap<>();

    private Map<String,List<NotifyListener>> notifyListeners = new ConcurrentHashMap<>();

    public ZookeeperRegistry(RegistryURL url,CuratorFramework client){
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
            String parentPath = null;

            String side = url.getParameter(URLParamType.side.getName());
            if(Constraint.SIDE_PROVIDER.equalsIgnoreCase(side)){
                parentPath = String.format("/luna/%s/%s/providers",url.getGroup(),url.getService());
            }else if(Constraint.SIDE_CONSUMER.equalsIgnoreCase(side)){
                parentPath = String.format("/luna/%s/%s/consumers",url.getGroup(),url.getService());
            }else{
                throw new LunaRpcException(String.format("%s have incorrect side : %s",url.toFullStr(),side));
            }
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

    @Override
    public void subscribe(URL url, NotifyListener listener) {
        try{
            ZookeeperWatcherManager watcherManager = watcherManagers.get(url.toString());
            if(watcherManager == null){
                watcherManager = new ZookeeperWatcherManager(this.url,this.client,url);
                watcherManagers.put(url.toString(),watcherManager);
                String parentPath = String.format("/luna/%s/%s/providers",url.getGroup(),url.getService());
                client.getChildren().usingWatcher(watcherManager).forPath(parentPath);
            }

            watcherManager.addNotifyListener(listener);
            watcherManager.reflushProviders();
        }catch (Exception e){
            throw new LunaRpcException(String.format("Failed to subscribe %s to zookeeper(%s)",url,this.url),e);
        }

    }
}
