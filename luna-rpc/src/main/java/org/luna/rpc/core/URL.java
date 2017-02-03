package org.luna.rpc.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.luna.rpc.common.constant.URLParamType;

/**
 * URL总线
 * Created by luliru on 2016/11/1.
 */
public class URL {

    protected String protocol;

    protected String host;

    protected int port;

    protected String service;

    protected String version;

    protected Map<String, String> parameters = new HashMap<>();

    public URL(String protocol, String host, int port, String group, String service, String version) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.service = service;
        this.version = version;
        if(StringUtils.isNotBlank(group)){
            parameters.put(URLParamType.group.getName(),group);
        }
    }

    public static URL valueOf(String url){
        if (StringUtils.isBlank(url)) {
            throw new LunaRpcException("url is null");
        }
        String protocol = null;
        String host = null;
        int port = 0;
        String path = null;
        Map<String, String> parameters = new HashMap<String, String>();;
        int i = url.indexOf("?"); // seperator between body and parameters
        if (i >= 0) {
            String[] parts = url.substring(i + 1).split("\\&");

            for (String part : parts) {
                part = part.trim();
                if (part.length() > 0) {
                    int j = part.indexOf('=');
                    if (j >= 0) {
                        parameters.put(part.substring(0, j), part.substring(j + 1));
                    } else {
                        parameters.put(part, part);
                    }
                }
            }
            url = url.substring(0, i);
        }
        i = url.indexOf("://");
        if (i >= 0) {
            if (i == 0) throw new IllegalStateException("url missing protocol: \"" + url + "\"");
            protocol = url.substring(0, i);
            url = url.substring(i + 3);
        } else {
            i = url.indexOf(":/");
            if (i >= 0) {
                if (i == 0) throw new IllegalStateException("url missing protocol: \"" + url + "\"");
                protocol = url.substring(0, i);
                url = url.substring(i + 1);
            }
        }

        i = url.indexOf("/");
        if (i >= 0) {
            path = url.substring(i + 1);
            url = url.substring(0, i);
        }

        i = url.indexOf(":");
        if (i >= 0 && i < url.length() - 1) {
            port = Integer.parseInt(url.substring(i + 1));
            url = url.substring(0, i);
        }
        if (url.length() > 0) host = url;
        return new URL(protocol, host, port,parameters.get(URLParamType.group.getName()),path, null);
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
        return getParameter(URLParamType.group.getName(),URLParamType.group.getValue());
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
