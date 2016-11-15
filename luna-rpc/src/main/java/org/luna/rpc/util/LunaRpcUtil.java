package org.luna.rpc.util;

import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.URL;

/**
 * Rpc工具类
 * Created by luliru on 2016/11/12.
 */
public class LunaRpcUtil {

    private LunaRpcUtil(){};

    /**
     * 获取服务唯一描述符
     * @param invocation
     * @return
     */
    public static String getUniqueServiceDecription(Invocation invocation){
        return String.format("%s/%s/%s",invocation.getApplication(),invocation.getServiceName(),invocation.getVersion());
    }

    /**
     * 获取服务唯一描述符
     * @param url
     * @return
     */
    public static String getUniqueServiceDecription(URL url){
        return String.format("%s/%s/%s",url.getService(),url.getApplication(),url.getService(),url.getVersion());
    }
}
