package org.luna.rpc.config;

/**
 * 注册中心配置
 * Created by luliru on 2016/12/30.
 */
public class RegistryConfig {

    /** 注册协议 */
    private String regProtocol;

    /** 注册中心地址，格式：ip_1:port_1,ip_2:port2 */
    private String address;

    public String getRegProtocol() {
        return regProtocol;
    }

    public void setRegProtocol(String regProtocol) {
        this.regProtocol = regProtocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
