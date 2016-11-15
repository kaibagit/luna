package org.luna.rpc.protocol;

import org.luna.rpc.core.Client;
import org.luna.rpc.core.Exporter;
import org.luna.rpc.core.Invoker;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.buildin.DefaultMessageHandler;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.core.extension.Spi;
import org.luna.rpc.transport.ServerTransport;
import org.luna.rpc.transport.TransportFactory;

/**
 * Created by luliru on 2016/11/1.
 */
@Spi(name = "luna")
public class DefaultRpcProtocol implements Protocol {

    private DefaultMessageHandler messageHandler = new DefaultMessageHandler();

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker, URL url) {
        Exporter<T> exporter = new DefaultRpcExporter(invoker,url);
        exporter.start();
        return exporter;
    }

    @Override
    public <T> Client<T> refer(Class<T> clz, URL url) {
        return null;
    }

    class DefaultRpcExporter<T> implements Exporter<T>{

        private ServerTransport serverTransport;

        private TransportFactory transportFactory;

        private Invoker<T> invoker;

        public DefaultRpcExporter(Invoker<T> invoker, URL url) {
            this.invoker = invoker;
            messageHandler.addInvoker(invoker);

            transportFactory =
                    ExtensionLoader.getExtension(TransportFactory.class);
            serverTransport = transportFactory.createServerTransport(url, messageHandler);
        }

        @Override
        public Invoker<T> getInvoker() {
            return invoker;
        }

        @Override
        public void unexport() {

        }

        @Override
        public void start() {
            serverTransport.start();
        }

        @Override
        public void destory() {

        }
    }

}