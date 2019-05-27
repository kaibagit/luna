package org.luna.rpc.protocol.spi;

import org.luna.rpc.core.*;
import org.luna.rpc.core.buildin.DefaultMessageHandler;
import org.luna.rpc.core.buildin.DefaultRpcResult;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.core.extension.Spi;
import org.luna.rpc.protocol.FutureAdapter;
import org.luna.rpc.protocol.Protocol;
import org.luna.rpc.transport.*;
import org.luna.rpc.util.RpcUtil;

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
        public void destroy() {
            serverTransport.destroy();
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
            DefaultRpcResult result = new DefaultRpcResult();

            Request request = new Request();
            request.setData(invocation);
            boolean isAsync = RpcUtil.isAsync(getUrl(),invocation);
            ResponseFuture responseFuture = clientTransport.send(request);
            if(isAsync){
                FutureAdapter futureAdapter = new FutureAdapter(responseFuture);
                result.setValue(null);
                RpcContext.getContext().setFuture(futureAdapter);
            }else{
                Object responseValue = responseFuture.get();  //阻塞直到获取返回值
                if(responseValue instanceof Exception){
                    result.setException((Exception)responseValue);
                }else{
                    result.setValue(responseValue);
                }
            }
            return result;
        }

        @Override
        public void start() {
            clientTransport.start();
        }

        @Override
        public void destroy() {
            clientTransport.destroy();
        }
    }

}