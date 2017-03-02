package org.luna.rpc.config;

import org.luna.rpc.common.constant.Constraint;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.Exporter;
import org.luna.rpc.core.Invoker;
import org.luna.rpc.core.exception.LunaRpcException;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.buildin.DefaultInvoker;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.protocol.FilterWrapperProtocol;
import org.luna.rpc.protocol.Protocol;
import org.luna.rpc.protocol.RateLimitingProtocol;
import org.luna.rpc.registry.Registry;
import org.luna.rpc.registry.RegistryFactory;
import org.luna.rpc.registry.RegistryURL;
import org.luna.rpc.util.NetUtil;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by kaiba on 2016/11/1.
 */
public class ServiceConfig<T> {

    private ApplicationConfig application;

    /** service分组 */
    private String group = Constraint.DEFAULT_GROUP;

    /** service接口类 */
    private Class<T> serviceClass;

    private String version = Constraint.DEFAULT_VERSION;

    /** 暴露、使用的协议 */
    private List<ProtocolConfig> protocols = new ArrayList<>();

    private List<MethodConfig> methods;

    /** 注册中心 */
    private RegistryConfig registry;

    /** service接口实现类 */
    private T ref;

    /** 服务限流 */
    private Double rateLimit;

    /** 提供Service的Worker Thread */
    private Integer workerThread;

    public List<ProtocolConfig> getProtocols() {
        return protocols;
    }

    private List<Exporter<T>> exporters = new CopyOnWriteArrayList<Exporter<T>>();

    public synchronized void export() {
        List<RegistryURL> registryList = loadRegistryUrls();

        List<URL> urls = new ArrayList<>();
        for(ProtocolConfig protocol : protocols){
            urls.add(createURL(protocol));
        }

        for(URL url : urls){
            exporters.add(doExporter(serviceClass,url,ref,registryList));
        }

    }

    private URL createURL(ProtocolConfig protocol){
        String hostAddress = protocol.getHost();
        if(hostAddress == null){
            InetAddress inetAddress = NetUtil.getLocalAddress();
            hostAddress = inetAddress.getHostAddress();
        }
        URL url = new URL(protocol.getName(),hostAddress,protocol.getPort(),group,serviceClass.getName(),version);
        url.addParameter(URLParamType.serialize.name(),protocol.getSerialization());
        url.addParameter(URLParamType.side.getName(),Constraint.SIDE_PROVIDER);
        if(rateLimit != null){
            url.addParameter(URLParamType.rateLimit.getName(),rateLimit.toString());
        }
        if(workerThread != null){
            url.addParameter(URLParamType.workerThread.getName(),workerThread.toString());
        }
        return url;
    }

    private Exporter<T> doExporter(Class<T> serviceClass, URL url, T ref, List<RegistryURL> registryList){
        Protocol protocol = ExtensionLoader.getExtension(Protocol.class,url.getProtocol());
        protocol = new FilterWrapperProtocol(protocol);
        protocol = new RateLimitingProtocol(protocol);
        Invoker<T> invoker = new DefaultInvoker<>(ref,url,serviceClass);
        Exporter<T> exporter = protocol.export(invoker, url);

        // register service
        for(RegistryURL registryUrl : registryList){
            RegistryFactory registryFactory = ExtensionLoader.getExtension(RegistryFactory.class,registryUrl.getProtocol());
            if(registryFactory == null){
                throw new LunaRpcException("Register error! Could not find extension for registry protocol : "+registryUrl.getProtocol());
            }
            Registry registry = registryFactory.getRegistry(registryUrl);
            registry.register(url);
        }

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setRegistry(RegistryConfig registry) {
        this.registry = registry;
    }

    public void setWorkerThread(Integer workerThread) {
        if(workerThread == null || workerThread <= 0){
            throw new IllegalArgumentException("illegal workerThread = "+workerThread);
        }
        this.workerThread = workerThread;
    }

    public void setRateLimit(Double rateLimit) {
        this.rateLimit = rateLimit;
    }

    /**
     * 加载注册URL
     * @return
     */
    private List<RegistryURL> loadRegistryUrls(){
        List<RegistryURL> registryList = new ArrayList<>();
        if(registry != null){
            String address = registry.getAddress();
            String[] ipAndPortArr = address.split(",");
            for(String ipAndPort : ipAndPortArr){
                String[] arr = ipAndPort.split(":");
                String ip = arr[0];
                int port = Integer.valueOf(arr[1]);
                RegistryURL url = new RegistryURL(registry.getRegProtocol(),ip,port);
                registryList.add(url);
            }
        }

        return registryList;
    }
}
