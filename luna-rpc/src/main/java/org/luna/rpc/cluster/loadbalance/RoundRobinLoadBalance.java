package org.luna.rpc.cluster.loadbalance;

import org.luna.rpc.cluster.LoadBalance;
import org.luna.rpc.core.Client;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.exception.LunaRpcException;
import org.luna.rpc.core.extension.Spi;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于轮询方式的负载均衡
 * Created by luliru on 2016/12/11.
 */
@Spi(name="roundRobinLoadBalance")
public class RoundRobinLoadBalance<T> implements LoadBalance<T> {

    private AtomicInteger idx = new AtomicInteger(0);

    @Override
    public Client<T> select(Invocation invocation, List<Client<T>> clients) {
        if(clients == null || clients.isEmpty()){
            throw new LunaRpcException(String.format("There are no providers of %s.",invocation.getServiceName()));
        }
        int round = idx.getAndIncrement();
        return clients.get(round % clients.size());
    }
}
