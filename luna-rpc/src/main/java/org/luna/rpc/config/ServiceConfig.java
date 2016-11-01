package org.luna.rpc.config;

import java.util.List;

/**
 * Created by kaiba on 2016/11/1.
 */
public class ServiceConfig<T> {

    private String group;

    /** 服务接口类 */
    private Class<T> serviceClass;

    private String version;

    /** 暴露、使用的协议 */
    private List<ProtocolConfig> protocols;

    private List<MethodConfig> methods;

    public List<ProtocolConfig> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<ProtocolConfig> protocols) {
        this.protocols = protocols;
    }

    public Class<T> getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class<T> serviceClass) {
        this.serviceClass = serviceClass;
    }
}
