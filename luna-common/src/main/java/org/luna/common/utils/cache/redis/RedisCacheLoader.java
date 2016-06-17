package org.luna.common.utils.cache.redis;

import org.luna.common.utils.cache.CacheLoadResult;
import org.luna.common.utils.cache.CacheLoader;
import org.luna.common.utils.cache.Serialization;
import redis.clients.jedis.Jedis;

/**
 * Created by luliru on 2016/6/3.
 */
public class RedisCacheLoader implements CacheLoader {

    private static final byte[] EMPTY_DATA = "&&".getBytes();

    private Jedis jedis;

    private Serialization serialization;

    public RedisCacheLoader(Jedis jedis,Serialization serialization){
        this.jedis = jedis;
        this.serialization = serialization;
    }

    public <T> CacheLoadResult<T> load(String key, Class<T> clazz) {
        CacheLoadResult result = null;
        byte[] redisData = jedis.get(key.getBytes());
        if(redisData == null){
            result = new CacheLoadResult(false,null);
        }else if(EMPTY_DATA.equals(redisData)){
            result = new CacheLoadResult(true,null);
        }else{
            T obj = serialization.deserialize(redisData,clazz);
            result = new CacheLoadResult(true,obj);
        }
        return null;
    }

    public void put(String key, Object obj) {
        if(obj == null){
            jedis.set(key.getBytes(),EMPTY_DATA);
        }else{
            jedis.set(key.getBytes(),serialization.serialize(obj));
        }
    }

}
