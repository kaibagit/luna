package org.luna.rpc.protocol;

import org.luna.rpc.Exporter;
import org.luna.rpc.Invoker;
import org.luna.rpc.URL;

/**
 * 协议
 * Created by kaiba on 2016/5/24.
 */
public interface Protocol {

    /**
     * 暴露夫妇
     * @param invoker
     * @param url
     * @param <T>
     * @return
     */
    <T> Exporter<T> export(Invoker invoker, URL url);

    /**
     * 引用服务
     *
     * @param <T>
     * @param clz
     * @param url
     * @return
     */
    <T> Exporter<T> refer(Class<T> clz, URL url);

}
