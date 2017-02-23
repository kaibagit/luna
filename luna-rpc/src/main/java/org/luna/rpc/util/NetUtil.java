package org.luna.rpc.util;

import org.luna.rpc.core.exception.LunaRpcException;

import java.net.InetAddress;

/**
 * 网络工具类
 * Created by luliru on 2017/1/20.
 */
public class NetUtil {

    private static volatile InetAddress LOCAL_ADDRESS = null;

    /**
     * 获取本地地址
     * @return
     */
    public static InetAddress getLocalAddress(){
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        }

        InetAddress localAddress = getLocalAddressByHostname();
        LOCAL_ADDRESS = localAddress;

        if(LOCAL_ADDRESS == null){
            throw new LunaRpcException("Please config local server hostname with intranet IP first!");
        }

        return localAddress;
    }

    private static InetAddress getLocalAddressByHostname() {
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            return localAddress;
        } catch (Throwable e) {
            LoggerUtil.warn("Failed to retriving local address by hostname:" + e);
        }
        return null;
    }
}
