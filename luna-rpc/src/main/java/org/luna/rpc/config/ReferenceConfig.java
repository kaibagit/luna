package org.luna.rpc.config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.luna.rpc.cluster.ClusterClient;
import org.luna.rpc.common.constant.Constraint;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.Client;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.protocol.FilterWrapperProtocol;
import org.luna.rpc.protocol.Protocol;
import org.luna.rpc.proxy.ProxyFactory;

/**
 * Created by luliru on 2016/10/14.
 */
public class ReferenceConfig<T> {

    /** service分组 */
    private String group = Constraint.DEFAULT_GROUP;

    /** 服务接口类 */
    private Class<T> serviceClass;

    private String version = "1.0";

    /** 使用的协议 */
    private String protocolName;

    private String serialization;

    // 具体到方法的配置
    private List<MethodConfig> methods = new ArrayList<>();

    /** 服务提供方的IP和端口，格式为：ip_1:port_1,ip_2:port_2 */
    private String urls;

    private T ref;

    public synchronized  void initRef(){
        if(ref != null){
            return;
        }
        checkParameters();

        List<Client<T>> clients = new ArrayList<>();
        String[] urlArr = urls.split(",");
        for(String url : urlArr){
            String[] ipAndPort = url.split(":");
            String ip = ipAndPort[0];
            int port = Integer.valueOf(ipAndPort[1]);
            URL refUrl = new URL(protocolName,ip,port,group,serviceClass.getName(),version);
            addParameters(refUrl);
            Protocol protocol = ExtensionLoader.getExtension(Protocol.class,protocolName);
            protocol = new FilterWrapperProtocol(protocol);
            Client<T> client = protocol.refer(serviceClass,refUrl);

            clients.add(client);
        }

        ProxyFactory proxyFactory = ExtensionLoader.getExtension(ProxyFactory.class);
        if(clients.size() > 1){
            URL url = new URL(protocolName,null,0,group,serviceClass.getName(),version);
            ClusterClient<T> clusterClient = new ClusterClient(url,clients);
            clusterClient.start();
            ref = proxyFactory.getProxy(serviceClass,clusterClient);
        }else{
            ref = proxyFactory.getProxy(serviceClass,clients.get(0));
        }
    }

    public Class<T> getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class<T> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<MethodConfig> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodConfig> methods) {
        this.methods = methods;
    }

    public T getRef() {
        if(ref == null){
            initRef();
        }
        return ref;
    }

    public String getProtocol() {
        return protocolName;
    }

    public void setProtocol(String protocolName) {
        this.protocolName = protocolName;
    }

    /**
     * 增加URL参数
     * @param refUrl
     */
    private void addParameters(URL refUrl){
        if(serialization != null){
            refUrl.addParameter(URLParamType.serialize.name(),serialization);
        }
        for(MethodConfig method : methods){
            String str = String.format("methods.%s.%s",method.getName(),URLParamType.async.getName());
            refUrl.addParameter(str,String.valueOf(method.isAsync()));
        }
    }

    /**
     * 检查参数
     */
    private void checkParameters(){
        if(urls == null || urls.trim().length() == 0){
            throw new LunaRpcException("Reference urls can't be blank.");
        }
        checkInterfaceAndMethods(serviceClass,methods);
    }

    /**
     * 检查接口和方法配置
     * @param serviceClass
     * @param methods
     */
    private void checkInterfaceAndMethods(Class<T> serviceClass,List<MethodConfig> methods){
        for(MethodConfig methodConfig : methods){
            boolean hasMethod = false;
            for(Method method : serviceClass.getMethods()){
                if(method.getName().equals(methodConfig.getName())){
                    hasMethod = true;
                    break;
                }
            }
            if(!hasMethod){
                throw new LunaRpcException("Service "+serviceClass.getName()+" not found method "+methodConfig.getName());
            }
        }
    }

    public String getSerialization() {
        return serialization;
    }

    public void setSerialization(String serialization) {
        this.serialization = serialization;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
