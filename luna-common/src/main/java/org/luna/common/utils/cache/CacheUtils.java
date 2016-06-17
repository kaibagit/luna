package org.luna.common.utils.cache;

/**
 * Created by luliru on 2016/6/3.
 */
public interface CacheUtils {

    public <T> T get(String key, Class<T> clazz);

    public <T> void put(String key, T obj);
}
