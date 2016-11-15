package org.luna.rpc.core.buildin;

import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.Invoker;

import java.util.Map;

/**
 * Created by kaiba on 2016/11/1.
 */
public class DefaultRpcInvocation implements Invocation {

    private String application;

    private String serviceName;

    private String version;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private Map<String, String> attachments;

    @Override
    public String getApplication() {
        return application;
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
        return null;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return new Class<?>[0];
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public Map<String, String> getAttachments() {
        return null;
    }

    @Override
    public String getAttachment(String key) {
        return null;
    }

    @Override
    public String getAttachment(String key, String defaultValue) {
        return null;
    }

    public void setApplication(String application) {
        this.application = application;
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
        this.parameterTypes = parameterTypes;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }
}
