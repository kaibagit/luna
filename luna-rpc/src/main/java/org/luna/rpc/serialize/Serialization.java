package org.luna.rpc.serialize;

/**
 * Created by kaiba on 2016/5/30.
 */
public interface Serialization {

//    public static Serialization getInstance(int SerializationType){
//        return null;
//    };

    public <T> T deserialize(byte[] bytes, Class<T> clazz);

    public byte[] serialize(Object object);
}
