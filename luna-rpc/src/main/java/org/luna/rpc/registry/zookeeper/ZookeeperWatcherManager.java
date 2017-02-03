package org.luna.rpc.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.core.URL;
import org.luna.rpc.registry.NotifyListener;
import org.luna.rpc.registry.RegistryURL;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kaiba on 2017/2/1.
 */
public class ZookeeperWatcherManager implements Watcher {

    private RegistryURL registryUrl;

    private URL serviceUrl;

    private CuratorFramework client;

    private List<NotifyListener> notifyListeners = new ArrayList<>();

    public ZookeeperWatcherManager(RegistryURL registryUrl,CuratorFramework client,URL serviceUrl){
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
        reflushProviders();
    }

    public void reflushProviders(){
        try{
            String parentPath = String.format("/luna/%s/%s/providers",serviceUrl.getGroup(),serviceUrl.getService());
            List<String> childrenNodeNameList = client.getChildren().forPath(parentPath);
            List<URL> providerUrlList = parseChildrenURLs(childrenNodeNameList);

            for(NotifyListener notifyListener : notifyListeners){
                notifyListener.notify(registryUrl,providerUrlList);
            }
        }catch (Exception e){
            throw new LunaRpcException(String.format("Failed to get %s providers of zookeeper(%s)",serviceUrl,registryUrl),e);
        }
    }

    private List<URL> parseChildrenURLs(List<String> childrenNodeNameList){
        try{
            List<URL> providerUrlList = new ArrayList<>();
            for(String childNodeName : childrenNodeNameList){
                String childPath = String.format("/luna/%s/%s/providers/%s",serviceUrl.getGroup(),serviceUrl.getService(),childNodeName);
                String urlStr = new String(client.getData().forPath(childPath));
                URL url = URL.valueOf(urlStr);
                providerUrlList.add(url);
            }
            return providerUrlList;
        }catch (Exception e){
            throw new LunaRpcException(String.format("Failed to parse %s children nodes of zookeeper(%s)",serviceUrl,registryUrl),e);
        }
    }
}
