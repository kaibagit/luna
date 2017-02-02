package org.luna.rpc.core;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

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

    private String group;

    private String service;

    private String version;

    private Map<String, String> parameters = new HashMap<>();

    public URL(String protocol, String host, int port, String group, String service, String version) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.group = group;
        this.service = service;
        this.version = version;
    }

    public static URL valueOf(String url){
        return null;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public String getParameter(String name,String defaultValue){
        String stringValue = parameters.get(name);
        if(stringValue == null || stringValue.trim().length() == 0){
            return defaultValue;
        }
        return stringValue;
    }

    public Integer getIntParameter(String name, int defaultValue) {
        String stringValue = parameters.get(name);
        if(stringValue == null || stringValue.trim().length() == 0){
            return defaultValue;
        }
        return Integer.valueOf(stringValue);
    }

    public Long getLongParameter(String name,long defaultValue){
        String stringValue = parameters.get(name);
        if(stringValue == null || stringValue.trim().length() == 0){
            return defaultValue;
        }
        return Long.valueOf(stringValue);
    }

    public Boolean getBooleanParameter(String name,boolean defaultValue){
        String stringValue = parameters.get(name);
        if(stringValue == null || stringValue.trim().length() == 0){
            return defaultValue;
        }
        return Boolean.valueOf(stringValue);
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

    public String getGroup() {
        return group;
    }

    public String getVersion() {
        return version;
    }

    public void addParameter(String name, String value) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value)) {
            return;
        }
        parameters.put(name, value);
    }

    public String toString(){
        return String.format("%s://%s:%d/%s",protocol,host,port,service);
    }

    public String toFullStr(){
        StringBuilder builder = new StringBuilder();
        builder.append(this.toString()).append("?");

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();

            builder.append(name).append("=").append(value).append("&");
        }

        char lastChar = builder.charAt(builder.length() - 1);
        if(lastChar == '?' || lastChar == '&'){
            builder.deleteCharAt(builder.length() -1);
        }

        return builder.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof URL)) {
            return false;
        }
        URL ou = (URL) obj;
        return this.toString().equals(ou.toString());
    }
}
