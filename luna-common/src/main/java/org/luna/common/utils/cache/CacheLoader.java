package org.luna.common.utils.cache;

/**
 * Created by luliru on 2016/6/3.
 */
public interface CacheLoader {

    public <T> CacheLoadResult<T> load(String key, Class<T> clazz);

    public void put(String key, Object obj);

}
