package org.luna.rpc.cluster;

import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.Client;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.Result;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.exception.LunaRpcException;
import org.luna.rpc.registry.RegistryURL;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * FailoverClusterClient
 * Created by kaiba on 2019/05/30.
 */
public class FailoverClusterClient<T> extends ClusterClient<T> {

    public FailoverClusterClient(Class<T> serviceClass, URL url, List<RegistryURL> registryList) {
        super(serviceClass, url, registryList);
    }

    @Override
    public Result call(Invocation invocation) {
        int retries = url.getIntParameter(URLParamType.retries.getName(),URLParamType.retries.getIntValue());
        Exception lastException = null;
        Set<Client<T>> invokedClients = new HashSet<>();
        for(int i=0;i<=retries;i++){
            Client<T> client = select(invocation,invokedClients);
            try{
                return client.call(invocation);
            }catch (Exception e){
                lastException = e;
            }
            invokedClients.add(client);
        }
        throw new LunaRpcException(
                String.format("Failed to call %s.%s with %d retries. Last exception is: %s:%s",
                        invocation.getServiceName(),invocation.getMethodName(),retries,lastException.getClass().getName(),lastException.getMessage()
                )
        );
    }
}
