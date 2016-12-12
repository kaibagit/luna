package org.luna.rpc.core;

import java.util.Map;

/**
 * Created by luliru on 2016/11/1.
 */
public interface Invocation {

    String getGroup();

    String getServiceName();

    String getVersion();

    /**
     * get method name.
     *
     * @serial
     * @return method name.
     */
    String getMethodName();

    /**
     * get parameter types.
     *
     * @serial
     * @return parameter types.
     */
    Class<?>[] getParameterTypes();

    /**
     * get arguments.
     *
     * @serial
     * @return arguments.
     */
    Object[] getArguments();

    /**
     * get attachments.
     *
     * @serial
     * @return attachments.
     */
    Map<String, String> getAttachments();

    /**
     * get attachment by key.
     *
     * @serial
     * @return attachment value.
     */
    String getAttachment(String key);

    /**
     * get attachment by key with default value.
     *
     * @serial
     * @return attachment value.
     */
    String getAttachment(String key, String defaultValue);

}
