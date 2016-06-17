package org.luna.common.utils.cache;

/**
 * Created by luliru on 2016/6/3.
 */
public interface Serialization {

    public byte[] serialize(Object obj);

    public <T> T deserialize(byte[] bytes, Class<T> clazz);
}
