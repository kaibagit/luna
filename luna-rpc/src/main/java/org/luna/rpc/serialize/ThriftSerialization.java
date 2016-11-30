package org.luna.rpc.serialize;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.core.extension.Spi;

/**
 * Created by luliru on 2016/11/30.
 */
@Spi(name = "thrift")
public class ThriftSerialization implements Serialization {

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        TDeserializer deserializer = new TDeserializer(new TBinaryProtocol.Factory());
        T obj = null;
        try {
            obj = clazz.newInstance();
            deserializer.deserialize((TBase) obj, bytes);
        } catch (Exception e) {
            throw new LunaRpcException("Deserialize fail.",e);
        }
        return obj;
    }

    @Override
    public byte[] serialize(Object object) {
        if(!(object instanceof TBase)){
            throw new LunaRpcException("ThriftSerialization not support serialize "+object);
        }
        TSerializer serializerBinary = new TSerializer(new TBinaryProtocol.Factory());
        byte[] bytes = null;
        try {
            bytes = serializerBinary.serialize((TBase)object);
        } catch (TException e) {
            throw new LunaRpcException("Serialize "+object+" fail.",e);
        }
        return bytes;
    }
}
