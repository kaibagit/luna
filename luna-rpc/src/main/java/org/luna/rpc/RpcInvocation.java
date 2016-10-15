package org.luna.rpc;

import java.util.Map;

/**
 * Created by luliru on 2016/10/15.
 */
public class RpcInvocation {

    private String interfaceName;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private Map<String, String> attachments;
}
