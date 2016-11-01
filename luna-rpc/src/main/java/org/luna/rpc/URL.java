package org.luna.rpc;

import java.util.Map;

/**
 * URL总线
 * Created by luliru on 2016/11/1.
 */
public class URL {

    private String protocol;

    private String host;

    private int port;

    private String group;

    private String service;

    private String version;

    private Map<String, String> parameters;

    public URL(String protocol, String host, int port, String group, String service, String version, Map<String, String> parameters) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.group = group;
        this.service = service;
        this.version = version;
        this.parameters = parameters;
    }

    public static URL valueOf(String url){
        return null;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getService() {
        return service;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
