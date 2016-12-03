package org.luna.rpc.util;

import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.URL;

/**
 * RPC工具
 * Created by luliru on 2016/12/3.
 */
public class RpcUtil {

    public static boolean isAsync(URL url, Invocation inv) {
        String str = String.format("methods.%s.%s",inv.getMethodName(), URLParamType.async.getName());
        Boolean async = url.getBooleanParameter(str,Boolean.valueOf(URLParamType.async.getValue()));
        return async;
    }
}
