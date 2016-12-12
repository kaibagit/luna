package org.luna.rpc.core.buildin;

import org.luna.rpc.core.Invocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaiba on 2016/11/1.
 */
public class DefaultRpcInvocation implements Invocation {

    private String group;

    private String serviceName;

    private String version;

    private String methodName;

    private Class<?>[] parameterTypes = new Class<?>[0];

    private Object[] arguments = new Object[0];

    private Map<String, String> attachments = new HashMap<>();

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Map<String, String> getAttachments() {
        return attachments;
    }

    @Override
    public String getAttachment(String key) {
        return attachments.get(key);
    }

    @Override
    public String getAttachment(String key, String defaultValue) {
        String value = attachments.get(key);
        if(value == null){
            value = defaultValue;
        }
        return value;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        if(parameterTypes != null && parameterTypes.length > 0){
            this.parameterTypes = parameterTypes;
        }
    }

    public void setArguments(Object[] arguments) {
        if(arguments != null && arguments.length > 0){
            this.arguments = arguments;
        }
    }

    public void setAttachments(Map<String, String> attachments) {
        if(attachments != null && !attachments.isEmpty()){
            this.attachments = attachments;
        }
    }
}
