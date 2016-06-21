package org.luna.rpc.serialize;

import com.alibaba.fastjson.JSON;

/**
 * Created by kaiba on 2016/5/30.
 */
public class JsonSerialization implements Serialization {

    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        String jsonStr = new String(bytes);
        T obj = JSON.parseObject(jsonStr,clazz);
        return obj;
    }

    public byte[] serialize(Object object) {
        String jsonStr = JSON.toJSONString(object);
        return jsonStr.getBytes();
    }
}
