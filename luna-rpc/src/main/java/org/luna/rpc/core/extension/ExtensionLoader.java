package org.luna.rpc.core.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 扩展加载器
 * Created by luliru on 2016/10/14.
 */
public class ExtensionLoader<T> {

    private static Map<Class,Map<String,Object>> shareMap = new ConcurrentHashMap<>();

    public static <T>List<T> getExtensions(Class<T> type){
        Map<String,Object> typeMap = getTypeMap(type);
        List<T> list = new ArrayList<T>();
        for(Map.Entry<String,Object> entry: typeMap.entrySet()){
            list.add((T) entry.getValue());
        }
        return list;
    }

    public static <T> T getExtension(Class<T> type) {
        Map<String,Object> typeMap = getTypeMap(type);
        for(Map.Entry<String,Object> entry: typeMap.entrySet()){
            return (T) entry.getValue();
        }
        return null;
    }

    public static <T> T getExtension(Class<T> type,String name){
        return getExtension(type,name,true);
    }

    /**
     * 获取扩展
     * @param type 扩展Class
     * @param name 扩展name
     * @param sharable 是否共享
     * @param <T>
     * @return
     */
    public static <T> T getExtension(Class<T> type,String name,boolean sharable){
        if(sharable){
            Map<String,Object> typeMap = getTypeMap(type);
            return (T) typeMap.get(name);
        }
        for(T t : ServiceLoader.load(type)){
            Spi annotation = t.getClass().getAnnotation(Spi.class);
            String spiName = annotation != null ? annotation.name() : "default";
            if(name.equals(spiName)){
                return t;
            }
        }
        return null;
    }

    private static <T> Map<String,Object> getTypeMap(Class<T> type){
        synchronized (type){
            Map<String,Object> typeMap = shareMap.get(type);
            if(typeMap == null){
                typeMap = new ConcurrentHashMap<>();
                shareMap.put(type,typeMap);
                for(T t : ServiceLoader.load(type)){
                    Spi annotation = t.getClass().getAnnotation(Spi.class);
                    String spiName = annotation != null ? annotation.name() : "default";
                    typeMap.put(spiName,t);
                }
            }
            return typeMap;
        }
    }
}
