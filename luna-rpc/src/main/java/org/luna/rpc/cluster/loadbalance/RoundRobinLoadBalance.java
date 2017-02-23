package org.luna.rpc.cluster.loadbalance;

import org.luna.rpc.cluster.LoadBalance;
import org.luna.rpc.core.Client;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.exception.LunaRpcException;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于轮询方式的负载均衡
 * Created by luliru on 2016/12/11.
 */
public class RoundRobinLoadBalance<T> implements LoadBalance<T> {

    private List<Client<T>> clients;

    private AtomicInteger idx = new AtomicInteger(0);

    public RoundRobinLoadBalance(List<Client<T>> clients){
        this.clients = clients;
    }

    @Override
    public Client<T> select(Invocation invocation) {
        if(clients == null || clients.size() == 0){
            throw new LunaRpcException(String.format("There are no providers of %s.",invocation.getServiceName()));
        }
        int round = idx.getAndIncrement();
        return clients.get(round % clients.size());
    }
}
