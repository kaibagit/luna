package org.luna.rpc.cluster.loadbalance;

import java.util.List;
import java.util.Random;

import org.luna.rpc.cluster.LoadBalance;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.Client;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.exception.LunaRpcException;
import org.luna.rpc.core.extension.Spi;

/**
 * 支持权重的负载均衡
 * Created by luliru on 2017/3/9.
 */
@Spi(name="weightLoadBalance")
public class WeightLoadBalance<T> implements LoadBalance<T> {

    private List<Client<T>> clients;

    private boolean sameWeight = true;

    private int totalWeight = 0;

    public WeightLoadBalance(){}

    public WeightLoadBalance(List<Client<T>> clients){
        init(clients);
    }

    @Override
    public Client<T> select(Invocation invocation) {
        if(clients == null || clients.isEmpty()){
            throw new LunaRpcException(String.format("There are no providers of %s.",invocation.getServiceName()));
        }
        Random random = new Random();
        if(sameWeight){
            return clients.get(random.nextInt(clients.size()));
        }
        int selectedIndex = random.nextInt(totalWeight);
        for(Client client : clients){
            int weight = client.getUrl().getIntParameter(URLParamType.weight.getName(),URLParamType.weight.getIntValue());
            selectedIndex -= weight;
            if(selectedIndex <= 0){
                return client;
            }
        }
        return null;
    }

    @Override
    public void onRefresh(List<Client<T>> clients) {
        init(clients);
    }

    private void init(List<Client<T>> clients){
        this.clients = clients;

        Integer lastestWeight = null;
        for(Client client : clients){
            URL url = client.getUrl();
            int weight = url.getIntParameter(URLParamType.weight.getName(),URLParamType.weight.getIntValue());
            totalWeight += weight;
            if(sameWeight && lastestWeight != null && lastestWeight != weight){
                sameWeight = false;
            }
            lastestWeight = weight;
        }
    }
}
