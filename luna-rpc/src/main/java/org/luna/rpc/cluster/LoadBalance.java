package org.luna.rpc.cluster;

import org.luna.rpc.core.Client;
import org.luna.rpc.core.Invocation;

import java.util.List;

/**
 * 负载均衡
 * Created by luliru on 2016/12/11.
 */
public interface LoadBalance<T> {

    Client<T> select(Invocation invocation);

    void onRefresh(List<Client<T>> clients);
}
