package org.luna.rpc.protocol;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.luna.rpc.codec.Codec;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.core.buildin.DefaultRpcInvocation;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.core.extension.Spi;
import org.luna.rpc.serialize.Serialization;
import org.luna.rpc.transport.Request;
import org.luna.rpc.transport.Transport;
import org.luna.rpc.transport.TransportBuffer;
import org.luna.rpc.util.ReflectUtil;

/**
 * Created by luliru on 2016/11/15.
 */
@Spi(name = "luna")
public class DefaultRpcCodec implements Codec {

    public static final short MAGIC = (short) 0x100a;

    public static final int HEAD_LENGTH = 160;

    /** 请求消息标记 */
    public static final byte FLAG_REQUEST = 0x00;

    /** 响应消息标记 */
    public static final byte FLAG_RESPONSE = 0x01;

    /** 单向消息标记 */
    public static final byte FLAG_ONEWAY = 0x02;

    /** 心跳消息标记，为长连接传输层心跳设计 */
    public static final byte FLAG_HEARTBEAT = 0x04;

    @Override
    public byte[] encode(Transport transport, Object message) throws IOException {
        return new byte[0];
    }

    @Override
    public Object decode(Transport transport, TransportBuffer buffer) throws IOException {
        if (buffer.readableBytes() < HEAD_LENGTH) {
            return null;
        }
        if(buffer.getShort(0) != MAGIC){
            throw new LunaRpcException("not support message magic");
        }
        int bodyLength = buffer.getInt(96);
        if(buffer.readableBytes() < HEAD_LENGTH + bodyLength){
            return null;
        }

        byte flag = buffer.getByte(16);
        if( (flag & FLAG_RESPONSE) == 0){
            long messageId = buffer.getLong(32);
            Request request = new Request(messageId);
            if( (flag & FLAG_ONEWAY) != 0){
                request.setOneway(true);
            }
            if( (flag & FLAG_HEARTBEAT) != 0 ){
                request.setHeartbeat(true);
            }else{
                byte[] body = new byte[bodyLength];
                buffer.getBytes(128,body,0,bodyLength);
                try {
                    Invocation invocation = decodeBody(transport,body);
                    request.setData(invocation);
                } catch (ClassNotFoundException e) {
                    throw new LunaRpcException("Class not find",e);
                }
            }
            return request;
        }else{

        }

        return null;
    }

    private Invocation decodeBody(Transport transport,byte[] body) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        ObjectInput input = createInput(inputStream);

        String application = input.readUTF();
        String serviceName = input.readUTF();
        String version = input.readUTF();
        String methodName= input.readUTF();
        String paramtersDesc = input.readUTF();

        DefaultRpcInvocation invocation = new DefaultRpcInvocation();
        Serialization serialization = ExtensionLoader.getExtension(
                Serialization.class,
                transport.getUrl().getParameter(URLParamType.serialize.name())
        );

        invocation.setApplication(application);
        invocation.setServiceName(serviceName);
        invocation.setVersion(version);
        invocation.setMethodName(methodName);
        if(paramtersDesc != null && paramtersDesc.length() > 0){
            Class<?>[] parameterTypes = ReflectUtil.forNames(paramtersDesc);
            Object[] parameters = new Object[parameterTypes.length];
            for(int i=0; i< parameterTypes.length; i++){
                Class<?> parameterType = parameterTypes[i];
                Object parameter = serialization.deserialize((byte[])input.readObject(),parameterType);
                parameters[i] = parameter;
            }
            invocation.setParameterTypes(parameterTypes);
            invocation.setArguments(parameters);
        }
        invocation.setAttachments(decodeRequestAttachments(input));

        return invocation;
    }

    public ObjectInput createInput(InputStream in) {
        try {
            return new ObjectInputStream(in);
        } catch (Exception e) {
            throw new LunaRpcException(this.getClass().getSimpleName() + " createInput error", e);
        }
    }

    private Map<String, String> decodeRequestAttachments(ObjectInput input) throws IOException, ClassNotFoundException {
        int size = input.readInt();

        if (size <= 0) {
            return null;
        }

        Map<String, String> attachments = new HashMap<String, String>();

        for (int i = 0; i < size; i++) {
            attachments.put(input.readUTF(), input.readUTF());
        }

        return attachments;
    }

}
