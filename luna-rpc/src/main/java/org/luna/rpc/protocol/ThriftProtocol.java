package org.luna.rpc.protocol;

import org.luna.rpc.core.Client;
import org.luna.rpc.core.Exporter;
import org.luna.rpc.core.Invoker;
import org.luna.rpc.core.URL;

/**
 * Created by kaiba on 2016/5/24.
 */
public class ThriftProtocol implements Protocol {

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker, URL url) {
        return null;
    }

    @Override
    public <T> Client<T> refer(Class<T> clz, URL url) {
        return null;
    }
}
