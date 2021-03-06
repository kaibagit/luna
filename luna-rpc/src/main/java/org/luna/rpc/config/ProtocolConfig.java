package org.luna.rpc.config;

/**
 * 协议配置
 * Created by kaiba on 2016/11/1.
 */
public class ProtocolConfig {

    // 服务协议
    private String name;

    private String host;

    private int port;

    // 序列化方式
    private String serialization;

    private int heartbeat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    public String getSerialization() {
        return serialization;
    }

    public void setSerialization(String serialization) {
        this.serialization = serialization;
    }
}
