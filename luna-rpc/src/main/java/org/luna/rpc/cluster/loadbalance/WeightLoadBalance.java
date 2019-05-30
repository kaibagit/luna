package org.luna.rpc.cluster.loadbalance;

import org.luna.rpc.cluster.LoadBalance;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.Client;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.extension.Spi;

import java.util.List;
import java.util.Random;

/**
 * 支持权重的负载均衡
 * Created by luliru on 2017/3/9.
 */
@Spi(name="weightLoadBalance")
public class WeightLoadBalance<T> implements LoadBalance<T> {

    private Random random = new Random();

    @Override
    public Client<T> select(Invocation invocation, List<Client<T>> clients) {
        int totalWeight = 0;
        boolean sameWeight = true;
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
}
