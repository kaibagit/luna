package org.luna.rpc.cluster;

import java.util.ArrayList;
import java.util.List;

import org.luna.rpc.cluster.loadbalance.RoundRobinLoadBalance;
import org.luna.rpc.core.Client;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.Result;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.protocol.FilterWrapperProtocol;
import org.luna.rpc.protocol.Protocol;

/**
 * Created by luliru on 2017/1/3.
 */
public class DirectClusterClient<T> implements Client<T> {

    private List<Client<T>> clients;

    private LoadBalance<T> loadBalance;

    private Class<T> serviceClass;

    private URL url;

    private List<URL> directUrls;

    public DirectClusterClient(Class<T> serviceClass,URL url,List<URL> directUrls){
        this.serviceClass = serviceClass;
        this.url = url;
        this.directUrls = directUrls;
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
        clients = new ArrayList<>();
        for(URL directUrl : directUrls){
            if(url == null){
                url = new URL(directUrl.getProtocol(),"0.0.0.0",0,directUrl.getGroup(),directUrl.getService(),directUrl.getVersion());
            }

            Protocol protocol = ExtensionLoader.getExtension(Protocol.class,directUrl.getProtocol());
            protocol = new FilterWrapperProtocol(protocol);
            Client<T> client = protocol.refer(serviceClass,directUrl);
            clients.add(client);
        }
        loadBalance = new RoundRobinLoadBalance<T>(clients);
    }

    @Override
    public void destory() {

    }

    public List<Client<T>> getClients() {
        return clients;
    }
}
