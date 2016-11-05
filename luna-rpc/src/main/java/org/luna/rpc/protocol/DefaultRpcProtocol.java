package org.luna.rpc.protocol;

import org.luna.rpc.core.Client;
import org.luna.rpc.core.Exporter;
import org.luna.rpc.core.Invoker;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.extension.Spi;

/**
 * Created by luliru on 2016/11/1.
 */
@Spi(name = "luna")
public class DefaultRpcProtocol implements Protocol {

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker, URL url) {
        return null;
    }

    @Override
    public <T> Client<T> refer(Class<T> clz, URL url) {
        return null;
    }

}