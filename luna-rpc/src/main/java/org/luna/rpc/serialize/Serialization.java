package org.luna.rpc.serialize;

/**
 * 序列化
 * Created by kaiba on 2016/5/30.
 */
public interface Serialization {

    public <T> T deserialize(byte[] bytes, Class<T> clazz);

    public byte[] serialize(Object object);
}
