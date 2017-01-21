package org.luna.rpc.cluster;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.luna.rpc.cluster.loadbalance.RoundRobinLoadBalance;
import org.luna.rpc.core.Client;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.Result;
import org.luna.rpc.core.URL;

import java.util.List;

/**
 * 支持集群的Client
 * Created by luliru on 2016/12/10.
 */
public class ClusterClient<T> implements Client<T>,Watcher {

    private Class<T> serviceClass;

    private List<Client<T>> clients;

    private LoadBalance<T> loadBalance;

    private List<URL> registryUrls;

    private URL url;

    public ClusterClient(Class<T> serviceClass,URL url,List<URL> registryList){
        this.serviceClass = serviceClass;
        this.url = url;
        this.registryUrls = registryList;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public Result call(Invocation invocation) {
        Client<T> client = loadBalance.select(invocation);
        return client.call(invocation);
    }

    @Override
    public void start() {
        loadBalance = new RoundRobinLoadBalance<T>(clients);
    }

    @Override
    public void destory() {

    }

    public List<Client<T>> getClients() {
        return clients;
    }

    @Override
    public void process(WatchedEvent event) {

    }
}
