package org.luna.rpc.config;

import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.Exporter;
import org.luna.rpc.core.Invoker;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.buildin.DefaultInvoker;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.protocol.FilterWrapperProtocol;
import org.luna.rpc.protocol.Protocol;
import org.luna.rpc.proxy.ProxyFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by kaiba on 2016/11/1.
 */
public class ServiceConfig<T> {

    private ApplicationConfig application;

    /** 服务接口类 */
    private Class<T> serviceClass;

    private String version = "1.0";

    /** 暴露、使用的协议 */
    private List<ProtocolConfig> protocols = new ArrayList<>();

    private List<MethodConfig> methods;

    public List<ProtocolConfig> getProtocols() {
        return protocols;
    }

    /** service接口实现类 */
    private T ref;

    private List<Exporter<T>> exporters = new CopyOnWriteArrayList<Exporter<T>>();

    public synchronized void export() {
        List<URL> urls = new ArrayList<>();
        for(ProtocolConfig protocol : protocols){
            urls.add(createURL(protocol));
        }

        for(URL url : urls){
            exporters.add(createExporter(serviceClass,url,ref));
        }

    }

    private URL createURL(ProtocolConfig protocol){
        URL url = new URL(protocol.getName(),protocol.getHost(),protocol.getPort(),application.getName(),serviceClass.getName(),version);
        url.addParameter(URLParamType.serialize.name(),protocol.getSerialization());
        return url;
    }

    private Exporter<T> createExporter(Class<T> serviceClass,URL url,T ref){
        Protocol protocol = ExtensionLoader.getExtension(Protocol.class,url.getProtocol());
        protocol = new FilterWrapperProtocol(protocol);
        Invoker<T> invoker = new DefaultInvoker<>(ref,url,serviceClass);
        Exporter<T> exporter = protocol.export(invoker, url);
        return exporter;
    }

    public void setProtocols(List<ProtocolConfig> protocols) {
        this.protocols = protocols;
    }

    public Class<T> getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class<T> serviceClass) {
        if (serviceClass != null && !serviceClass.isInterface()) {
            throw new LunaRpcException("The service class " + serviceClass + " is not a interface!");
        }
        this.serviceClass = serviceClass;
    }

    public void addProtocol(ProtocolConfig protocol){
        protocols.add(protocol);
    }

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    class ProtocolAndPort{
        private String protocol;
        private int prot;

        ProtocolAndPort(String protocol, int port){
            this.protocol = protocol;
            this.prot = port;
        }

        public String getProtocol() {
            return protocol;
        }

        public int getProt() {
            return prot;
        }
    }

    public ApplicationConfig getApplication() {
        return application;
    }

    public void setApplication(ApplicationConfig application) {
        this.application = application;
    }
}
