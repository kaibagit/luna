package org.luna.rpc.core.extension;

import java.util.ServiceLoader;

/**
 * 扩展加载器
 * Created by luliru on 2016/10/14.
 */
public class ExtensionLoader<T> {

    public static <T> T getExtension(Class<T> type) {
        T target = null;
        for(T t : ServiceLoader.load(type)){
            target = t;
            break;
        }
        return target;
    }
}
