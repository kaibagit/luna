package org.luna.rpc.core;

import java.util.Map;

/**
 * Created by kaiba on 2016/11/1.
 */
public class DefaultRpcInvocation implements Invocation {

    private String               methodName;

    private Class<?>[]           parameterTypes;

    private Object[]             arguments;

    private Map<String, String>  attachments;

    private transient Invoker<?> invoker;

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
}
