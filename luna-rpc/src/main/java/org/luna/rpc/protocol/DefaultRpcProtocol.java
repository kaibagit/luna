package org.luna.rpc.protocol;

import org.luna.rpc.core.Exporter;
import org.luna.rpc.core.Invoker;
import org.luna.rpc.core.URL;

/**
 * Created by luliru on 2016/11/1.
 */
public class DefaultRpcProtocol implements Protocol {

    @Override
    public <T> Exporter<T> export(Invoker invoker, URL url) {
        return null;
    }

    @Override
    public <T> Exporter<T> refer(Class<T> clz, URL url) {
        return null;
    }

}
