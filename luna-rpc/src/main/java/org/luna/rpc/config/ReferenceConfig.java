package org.luna.rpc.config;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.luna.rpc.cluster.ClusterClient;
import org.luna.rpc.cluster.DirectClusterClient;
import org.luna.rpc.common.constant.Constraint;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.Client;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.protocol.Protocol;
import org.luna.rpc.proxy.ProxyFactory;
import org.luna.rpc.registry.RegistryURL;
import org.luna.rpc.util.NetUtil;

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
    private String direct;

    /** 注册中心 */
    private RegistryConfig registry;

    private T ref;

    public synchronized  void initRef(){
        if(ref != null){
            return;
        }
        checkParameters();

        Client<T> client = null;

        if(StringUtils.isNotBlank(direct)){
            List<URL> directUrls = new ArrayList<>();
            String[] urlArr = direct.split(",");
            for(String url : urlArr){
                String[] ipAndPort = url.split(":");
                String ip = ipAndPort[0];
                int port = Integer.valueOf(ipAndPort[1]);
                URL refUrl = new URL(protocolName,ip,port,group,serviceClass.getName(),version);
                addParameters(refUrl);

                directUrls.add(refUrl);
            }

            DirectClusterClient directClusterClient = new DirectClusterClient<T>(serviceClass,null,directUrls);
            directClusterClient.start();

            client = directClusterClient;
        }else{
            List<RegistryURL> registryList = loadRegistryUrls();
            InetAddress inetAddress = NetUtil.getLocalAddress();
            String hostAddress = inetAddress.getHostAddress();
            URL url = new URL(protocolName,hostAddress,0,group,serviceClass.getName(),version);
            url.addParameter(URLParamType.side.getName(),Constraint.SIDE_CONSUMER);
            ClusterClient<T> clusterClient = new ClusterClient(serviceClass,url,registryList);
            clusterClient.start();

            client = clusterClient;
        }

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
        if((direct == null || direct.trim().length() == 0) && registry == null){
            throw new LunaRpcException("Reference direct or registry can't be null.");
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

    /**
     * 加载注册URL
     * @return
     */
    private List<RegistryURL> loadRegistryUrls(){
        List<RegistryURL> registryList = new ArrayList<RegistryURL>();
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

    public String getSerialization() {
        return serialization;
    }

    public void setSerialization(String serialization) {
        this.serialization = serialization;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
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
}
