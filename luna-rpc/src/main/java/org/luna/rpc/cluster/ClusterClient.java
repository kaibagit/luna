package org.luna.rpc.cluster;

import org.luna.rpc.cluster.loadbalance.RoundRobinLoadBalance;
import org.luna.rpc.core.*;
import org.luna.rpc.core.exception.LunaRpcException;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.protocol.FilterWrapperProtocol;
import org.luna.rpc.protocol.Protocol;
import org.luna.rpc.registry.NotifyListener;
import org.luna.rpc.registry.Registry;
import org.luna.rpc.registry.RegistryFactory;
import org.luna.rpc.registry.RegistryURL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支持集群的Client
 * Created by luliru on 2016/12/10.
 */
public class ClusterClient<T> implements Client<T>,NotifyListener {

    private Class<T> serviceClass;

    private LoadBalance<T> loadBalance;

    private List<RegistryURL> registryUrls;

    private URL url;

    private ConcurrentHashMap<URL, List<Client<T>>> registryClients = new ConcurrentHashMap<>();

    public ClusterClient(Class<T> serviceClass,URL url,List<RegistryURL> registryList){
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
        for(RegistryURL registryUrl : registryUrls){
            RegistryFactory registryFactory = ExtensionLoader.getExtension(RegistryFactory.class,registryUrl.getProtocol());
            if(registryFactory == null){
                throw new LunaRpcException("There is no RegistryFactory extension named " + registryUrl.getProtocol());
            }
            Registry registry = registryFactory.getRegistry(registryUrl);
            registry.subscribe(url,this);
        }
    }

    @Override
    public void destory() {
        for(Map.Entry<URL,List<Client<T>>> entry : registryClients.entrySet()){
            List<Client<T>> clientList = entry.getValue();
            for(Client<T> client : clientList){
                client.destory();
            }
        }
    }

    @Override
    public void notify(RegistryURL registryUrl, List<URL> urls) {
        List<Client<T>> oldClients = registryClients.get(registryUrl);
        if(oldClients == null){
            oldClients = new ArrayList<>();
        }
        List<Client<T>> destoryClients = new ArrayList<>();
        for(Client c : oldClients){
            destoryClients.add(c);  //默认所有的client都需要destroy
        }
        List<Client<T>> newClients = new ArrayList<>();
        for(URL u : urls){
            Client<T> client = null;
            for(int i=0;i<destoryClients.size();i++){
                Client<T> c = destoryClients.get(i);
                if(c.getUrl().equals(u)){
                    client = destoryClients.remove(i);
                }
            }
            if(client == null){
                Protocol protocol = ExtensionLoader.getExtension(Protocol.class,url.getProtocol());
                protocol = new FilterWrapperProtocol(protocol);
                client = protocol.refer(serviceClass,u);
                client.start();
            }
            newClients.add(client);
        }

        registryClients.put(registryUrl,newClients);
        loadBalance = new RoundRobinLoadBalance<>(newClients);
        for(Client<T> c : destoryClients){
            c.destory();
        }
    }
}
