package org.luna.rpc.config;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private String application;

    /** 服务接口类 */
    private Class<T> serviceClass;

    private String version = "1.0";

    /** 使用的协议 */
    private ProtocolConfig protocol;

    // 具体到方法的配置
    private List<MethodConfig> methods;

    // 点对点直连服务提供地址
    private String directUrl;

    private T ref;

    public synchronized  void initRef(){
        if(ref != null){
            return;
        }
        checkInterfaceAndMethods(serviceClass,methods);

        ProtocolConfig protocolConfig = protocol;
        URL refUrl = new URL(protocolConfig.getName(),protocolConfig.getHost(),protocolConfig.getPort(),application,serviceClass.getName(),version);
        addParameters(refUrl);
        Protocol protocol = ExtensionLoader.getExtension(Protocol.class,protocolConfig.getName());
        protocol = new FilterWrapperProtocol(protocol);
        Client<T> client = protocol.refer(serviceClass,refUrl);
        ProxyFactory proxyFactory = ExtensionLoader.getExtension(ProxyFactory.class);
        ref = proxyFactory.getProxy(serviceClass,client);
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

    public String getDirectUrl() {
        return directUrl;
    }

    public void setDirectUrl(String directUrl) {
        this.directUrl = directUrl;
    }

    public T getRef() {
        if(ref == null){
            initRef();
        }
        return ref;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public ProtocolConfig getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolConfig protocol) {
        this.protocol = protocol;
    }

    /**
     * 增加URL参数
     * @param refUrl
     */
    private void addParameters(URL refUrl){
        refUrl.addParameter(URLParamType.serialize.name(),protocol.getSerialization());
        for(MethodConfig method : methods){
            String str = String.format("methods.%s.%s",method.getName(),URLParamType.async.getName());
            refUrl.addParameter(str,String.valueOf(method.isAsync()));
        }
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
}
