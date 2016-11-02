package org.luna.rpc.config;

import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.core.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private List<ProtocolAndPort> protocolAndPortList = new ArrayList<>();

    public synchronized void export() {
//        if (exported.get()) {
//            LoggerUtil.warn(String.format("%s has already been expoted, so ignore the export request!", interfaceClass.getName()));
//            return;
//        }
//
//        checkInterfaceAndMethods(interfaceClass, methods);
//
//        List<URL> registryUrls = loadRegistryUrls();
//        if (registryUrls == null || registryUrls.size() == 0) {
//            throw new IllegalStateException("Should set registry config for service:" + interfaceClass.getName());
//        }
//
//        Map<String, Integer> protocolPorts = getProtocolAndPort();
//        for (ProtocolConfig protocolConfig : protocols) {
//            Integer port = protocolPorts.get(protocolConfig.getId());
//            if (port == null) {
//                throw new MotanServiceException(String.format("Unknow port in service:%s, protocol:%s", interfaceClass.getName(),
//                        protocolConfig.getId()));
//            }
//            doExport(protocolConfig, port, registryUrls);
//        }
//
//        afterExport();

        List<URL> urls = new ArrayList<>();
        for(ProtocolConfig protocol : protocols){
            urls.add(createURL(protocol));
        }

        for(URL url : urls){

        }

    }

    private URL createURL(ProtocolConfig protocol){
        URL url = new URL(protocol.getName(),protocol.getHost(),protocol.getPort(),application.getName(),serviceClass.getName(),version);
        return url;
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

    public void addProtocolAndPort(String protocol, int port){
        ProtocolAndPort pap = new ProtocolAndPort(protocol,port);
        protocolAndPortList.add(pap);
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
