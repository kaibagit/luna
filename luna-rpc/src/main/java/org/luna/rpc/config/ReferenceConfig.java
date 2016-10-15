package org.luna.rpc.config;

import java.util.List;

/**
 * Created by luliru on 2016/10/14.
 */
public class ReferenceConfig<T> {

    private Class<T> interfaceClass;

    // 具体到方法的配置
    protected List<MethodConfig> methods;

    // 点对点直连服务提供地址
    private String directUrl;

    private T ref;

}
