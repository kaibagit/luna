package org.luna.common.utils.cache.redis;


import org.luna.common.utils.cache.CacheLoadResult;
import org.luna.common.utils.cache.CacheLoader;
import org.luna.common.utils.cache.CacheUtils;
import org.luna.common.utils.cache.DataLoader;

/**
 * Created by luliru on 2016/6/3.
 */
public class RedisCacheUtils implements CacheUtils {

    private CacheLoader cacheLoader;

    private DataLoader dataLoader;

    public RedisCacheUtils(CacheLoader cacheLoader, DataLoader dataLoader){
        this.cacheLoader = cacheLoader;
        this.dataLoader = dataLoader;
    }

    public <T> T get(String key, Class<T> clazz) {
        T result = null;
        CacheLoadResult<T> loadResult = cacheLoader.load(key,clazz);
        //缓存中没有数据
        if(!loadResult.isHasData()){
            key = key.intern();
            //缓存不存在，需要从DB读取，加锁防止雪崩
            synchronized (key){
                //可能会出现上一次已经从DB中加载到Cache中了，所以再判断一次
                loadResult = cacheLoader.load(key,clazz);
                if(!loadResult.isHasData()){
                    T obj = loadFromDb(key,clazz);
                    cacheLoader.put(key,obj);
                    result = obj;
                }else{
                    result = loadResult.getData();
                }
            }
        }else{
            result = loadResult.getData();
        }
        return result;
    }

    public <T> void put(String key, T obj) {
        cacheLoader.put(key,obj);
    }

    private <T> T loadFromDb(String key, Class<T> clazz){
        return dataLoader.load(key,clazz);
    }
}
