package org.luna.rpc.serialize;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TIOStreamTransport;
import org.apache.thrift.transport.TMemoryInputTransport;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.core.extension.Spi;

import java.io.ByteArrayOutputStream;

/**
 * Created by luliru on 2016/11/30.
 */
@Spi(name = "thrift")
public class ThriftSerialization implements Serialization {

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        T obj = null;
        try{
            if(clazz == Integer.class || clazz == Integer.TYPE){
                obj = (T) deserializeToInt(bytes);
            }else if(clazz == Long.class || clazz == Long.TYPE){
                obj = (T) deserializeToLong(bytes);
            }else if(TBase.class.isAssignableFrom(clazz)){
                TDeserializer deserializer = new TDeserializer(new TBinaryProtocol.Factory());
                obj = clazz.newInstance();
                deserializer.deserialize((TBase) obj, bytes);
            }else{
                throw new LunaRpcException("ThriftSerialization not support deserialize this bytes.");
            }
        }catch (Exception e) {
            throw new LunaRpcException("Deserialize fail.",e);
        }
        return obj;
    }

    @Override
    public byte[] serialize(Object object) {
        byte[] bytes = null;
        try{
            if(object instanceof Integer){
                bytes = serializeInt((Integer)object);
            }else if(object instanceof Long){
                bytes = serializeLong((Long)object);
            }else if(object instanceof TBase){
                TSerializer serializerBinary = new TSerializer(new TBinaryProtocol.Factory());
                bytes = serializerBinary.serialize((TBase)object);
            }else{
                throw new LunaRpcException("ThriftSerialization not support serialize "+object);
            }
        }catch (TException e) {
            throw new LunaRpcException("Serialize "+object+" fail.",e);
        }

        return bytes;
    }

    private byte[] serializeInt(Integer value) throws TException {
        ByteArrayOutputStream baos_ = new ByteArrayOutputStream();
        TIOStreamTransport transport_ = new TIOStreamTransport(baos_);
        TProtocol protocol_ = new TBinaryProtocol.Factory().getProtocol(transport_);
        protocol_.writeI32(value);
        return baos_.toByteArray();
    }

    private Integer deserializeToInt(byte[] bytes) throws TException {
        TMemoryInputTransport trans_ = new TMemoryInputTransport();
        TProtocol protocol_ = new TBinaryProtocol.Factory().getProtocol(trans_);
        trans_.reset(bytes, 0, bytes.length);
        return protocol_.readI32();
    }

    private byte[] serializeLong(Long value) throws TException {
        ByteArrayOutputStream baos_ = new ByteArrayOutputStream();
        TIOStreamTransport transport_ = new TIOStreamTransport(baos_);
        TProtocol protocol_ = new TBinaryProtocol.Factory().getProtocol(transport_);
        protocol_.writeI64(value);
        return baos_.toByteArray();
    }

    private Long deserializeToLong(byte[] bytes) throws TException {
        TMemoryInputTransport trans_ = new TMemoryInputTransport();
        TProtocol protocol_ = new TBinaryProtocol.Factory().getProtocol(trans_);
        trans_.reset(bytes, 0, bytes.length);
        return protocol_.readI64();
    }
}
