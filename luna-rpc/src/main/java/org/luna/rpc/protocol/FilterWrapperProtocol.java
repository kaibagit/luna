package org.luna.rpc.protocol;

import org.luna.rpc.core.*;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.filter.Filter;
import org.luna.rpc.filter.FilterComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by luliru on 2016/11/4.
 */
public class FilterWrapperProtocol implements Protocol {

    private Protocol protocol;

    public FilterWrapperProtocol(Protocol protocol){
        this.protocol = protocol;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker, URL url) {
        return protocol.export(wrapInvokerWithFilters(invoker,url),url);
    }

    @Override
    public <T> Client<T> refer(Class<T> clz, URL url) {
        return wrapClientWithFilters(protocol.refer(clz,url),url);
    }

    private <T> Invoker<T> wrapInvokerWithFilters(Invoker<T> invoker,final URL url){
        Invoker<T> lastInvoker = invoker;
        List<Filter> filters = getInvokerFilters(url);
        for(Filter filter : filters){
            final Invoker<T> tempFilter = lastInvoker;
            lastInvoker = new Invoker<T>() {
                @Override
                public Class<T> getInterface() {
                    return tempFilter.getInterface();
                }

                @Override
                public URL getUrl() {
                    return url;
                }

                @Override
                public Result call(Invocation invocation) {
                    return filter.filter(tempFilter,invocation);
                }
            };
        }
        return lastInvoker;
    }

    private <T> Client<T> wrapClientWithFilters(Client<T> client,URL url){
        Client<T> lastClient = client;
        List<Filter> filters = getClientFilters(url);
        for(Filter filter : filters){
            final Client tempClient = lastClient;
            lastClient = new Client<T>() {
                @Override
                public URL getUrl() {
                    return url;
                }

                @Override
                public Result call(Invocation invocation) {
                    return filter.filter(tempClient,invocation);
                }
            };
        }
        return lastClient;
    }

    private List<Filter> getInvokerFilters(URL url){
        List<Filter> invokerFilters = new ArrayList<>();
        List<Filter> filters = ExtensionLoader.getExtensions(Filter.class);
        for(Filter filter : filters){
            if(filter.filterInvoker()){
                invokerFilters.add(filter);
            }
        }
        Collections.sort(invokerFilters,new FilterComparator());
        return invokerFilters;
    }

    private List<Filter> getClientFilters(URL url){
        List<Filter> clientFilters = new ArrayList<>();
        List<Filter> filters = ExtensionLoader.getExtensions(Filter.class);
        for(Filter filter : filters){
            if(filter.filterClient()){
                clientFilters.add(filter);
            }
        }
        Collections.sort(clientFilters,new FilterComparator());
        return clientFilters;
    }

}
