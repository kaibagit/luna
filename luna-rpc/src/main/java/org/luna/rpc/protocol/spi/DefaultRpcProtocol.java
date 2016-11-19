package org.luna.rpc.protocol.spi;

import org.luna.rpc.core.*;
import org.luna.rpc.core.buildin.DefaultMessageHandler;
import org.luna.rpc.core.buildin.DefaultRpcResult;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.core.extension.Spi;
import org.luna.rpc.protocol.Protocol;
import org.luna.rpc.transport.*;

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
        Client<T> client = new DefaultRpcClient<T>(clz,url);
        client.start();
        return client;
    }

    class DefaultRpcExporter<T> implements Exporter<T>{

        private ServerTransport serverTransport;

        private TransportFactory transportFactory;

        private Invoker<T> invoker;

        public DefaultRpcExporter(Invoker<T> invoker, URL url) {
            this.invoker = invoker;
            messageHandler.addInvoker(invoker);

            transportFactory = ExtensionLoader.getExtension(TransportFactory.class);
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
            serverTransport.destory();
        }
    }

    class DefaultRpcClient<T> implements Client<T>{

        private URL url;

        private TransportFactory transportFactory;

        private ClientTransport clientTransport;

        public DefaultRpcClient(Class<T> clz, URL url){
            this.url = url;
            transportFactory = ExtensionLoader.getExtension(TransportFactory.class);
            clientTransport = transportFactory.createClientTransport(url);
        }

        @Override
        public URL getUrl() {
            return url;
        }

        @Override
        public Result call(Invocation invocation) {
            Request request = new Request();
            request.setData(invocation);
            Response response = clientTransport.send(request);

            DefaultRpcResult result = new DefaultRpcResult();
            result.setException(response.getException());
            result.setValue(response.getValue());
            return result;
        }

        @Override
        public void start() {
            clientTransport.start();
        }

        @Override
        public void destory() {
            clientTransport.destory();
        }
    }

}