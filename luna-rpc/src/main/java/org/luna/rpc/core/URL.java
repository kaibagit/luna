package org.luna.rpc.core;

import java.util.HashMap;
import java.util.Map;

/**
 * URL总线
 * Created by luliru on 2016/11/1.
 */
public class URL {

    private String protocol;

    private String host;

    private int port;

    private String application;

    private String service;

    private String version;

    private Map<String, String> parameters = new HashMap<>();

    public URL(String protocol, String host, int port, String application, String service, String version) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.application = application;
        this.service = service;
        this.version = version;
    }

    public static URL valueOf(String url){
        return null;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public Integer getIntParameter(String name, int defaultValue) {
        String stringValue = parameters.get(name);
        if(stringValue == null || stringValue.trim().length() == 0){
            return defaultValue;
        }
        return Integer.valueOf(stringValue);
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

    public String getApplication() {
        return application;
    }

    public String getVersion() {
        return version;
    }
}
