package org.luna.rpc.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.core.URL;
import org.luna.rpc.registry.NotifyListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaiba on 2017/2/1.
 */
public class ZookeeperWatcherManager implements Watcher {

    private URL registryUrl;

    private URL serviceUrl;

    private CuratorFramework client;

    private List<NotifyListener> notifyListeners = new ArrayList<>();

    public ZookeeperWatcherManager(URL registryUrl,CuratorFramework client,URL serviceUrl){
        this.registryUrl = registryUrl;
        this.serviceUrl = serviceUrl;
        this.client = client;
    }

    public void addNotifyListener(NotifyListener listener){
        notifyListeners.add(listener);
    }

    public void removeNotifyListener(NotifyListener listener){
        notifyListeners.remove(listener);
    }



    @Override
    public void process(WatchedEvent event) {
        try{
            String parentPath = String.format("/luna/%s/%s/providers",serviceUrl.getGroup(),serviceUrl.getService());
            List<String> childrenNodeNameList = client.getChildren().forPath(parentPath);
            for(String childrenNodeName : childrenNodeNameList){

            }
        }catch (Exception e){
            throw new LunaRpcException(String.format("Failed to process %s node change of zookeeper(%s)",serviceUrl,registryUrl),e);
        }

    }
}
