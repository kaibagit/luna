package org.luna.rpc.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.exception.LunaRpcException;
import org.luna.rpc.registry.NotifyListener;
import org.luna.rpc.registry.RegistryURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kaiba on 2017/2/1.
 */
public class ZookeeperWatcherManager implements PathChildrenCacheListener {

    private static Logger logger = LoggerFactory.getLogger(ZookeeperWatcherManager.class);

    private RegistryURL registryUrl;

    private URL serviceUrl;

    private CuratorFramework client;

    private List<NotifyListener> notifyListeners = new ArrayList<>();

    private PathChildrenCache pathChildrenCache;

    private boolean inited = false;

    public ZookeeperWatcherManager(RegistryURL registryUrl,CuratorFramework client,PathChildrenCache pathChildrenCache,URL serviceUrl){
        this.registryUrl = registryUrl;
        this.serviceUrl = serviceUrl;
        this.client = client;
        this.pathChildrenCache = pathChildrenCache;
    }

    public void addNotifyListener(NotifyListener listener){
        notifyListeners.add(listener);
    }

    public void removeNotifyListener(NotifyListener listener){
        notifyListeners.remove(listener);
    }

    public void reflushProviders(){
        try{
            List<URL> providerUrlList = null;
            if(inited){
                List<ChildData> childDataList = pathChildrenCache.getCurrentData();
                if(childDataList == null){
                    childDataList = Collections.emptyList();
                }
                providerUrlList = parsechildDataList(childDataList);
            }else{
                String parentPath = String.format("/luna/%s/%s/providers",serviceUrl.getGroup(),serviceUrl.getService());
                List<String> childrenNodeNameList = client.getChildren().forPath(parentPath);
                providerUrlList = parseChildrenURLs(childrenNodeNameList);
                inited = true;
            }

            for(NotifyListener notifyListener : notifyListeners){
                notifyListener.notify(registryUrl,providerUrlList);
            }
        }catch (Exception e){
            throw new LunaRpcException(String.format("Failed to get %s providers of zookeeper(%s)",serviceUrl,registryUrl),e);
        }
    }

    private List<URL> parsechildDataList(List<ChildData> childDataList){
        try{
            List<URL> providerUrlList = new ArrayList<>();
            for(ChildData childData : childDataList){
                URL url = URL.valueOf(new String(childData.getData()));
                providerUrlList.add(url);
            }
            return providerUrlList;
        }catch (Exception e){
            throw new LunaRpcException(String.format("Failed to parse %s children nodes of zookeeper(%s)",serviceUrl,registryUrl),e);
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

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        logger.debug("zookeeper emited event, type:{},data:{}",event.getType(),event.getData());

        reflushProviders();
    }
}
