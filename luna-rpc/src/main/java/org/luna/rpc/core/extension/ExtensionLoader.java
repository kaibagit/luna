package org.luna.rpc.core.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 扩展加载器
 * Created by luliru on 2016/10/14.
 */
public class ExtensionLoader<T> {

    public static <T>List<T> getExtensions(Class<T> type){
        List<T> list = new ArrayList<T>();
        for(T t : ServiceLoader.load(type)){
            list.add(t);
        }
        return list;
    }

    public static <T> T getExtension(Class<T> type) {
        T target = null;
        for(T t : ServiceLoader.load(type)){
            target = t;
            break;
        }
        return target;
    }

    public static <T> T getExtension(Class<T> type,String name){
        T target = null;
        for(T t : ServiceLoader.load(type)){
            if(name.equals( t.getClass().getAnnotation(Spi.class).name() ) ){
                target = t;
                break;
            }
        }
        return target;
    }
}
