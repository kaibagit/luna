package org.luna.rpc.protocol.spi;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.luna.rpc.codec.Codec;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.buildin.DefaultRpcInvocation;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.core.extension.Spi;
import org.luna.rpc.serialize.Serialization;
import org.luna.rpc.transport.Request;
import org.luna.rpc.transport.Response;
import org.luna.rpc.transport.Transport;
import org.luna.rpc.transport.TransportBuffer;
import org.luna.rpc.util.ByteUtil;
import org.luna.rpc.util.ReflectUtil;

/**
 * 默认的RPC编码器
 * Created by luliru on 2016/11/15.
 */
@Spi(name = "luna")
public class DefaultRpcCodec implements Codec {

    public static final short MAGIC = (short) 0x100a;

    public static final int HEAD_LENGTH = 16;

    /** 请求消息标记 */
    public static final byte FLAG_REQUEST = 0x00;

    /** 响应消息标记 */
    public static final byte FLAG_RESPONSE = 0x01;

    /** 单向消息标记 */
    public static final byte FLAG_ONEWAY = 0x02;

    /** 心跳消息标记，为长连接传输层心跳设计 */
    public static final byte FLAG_HEARTBEAT = 0x04;

    /** 响应状态，调用成功 */
    public static final byte STATUS_OK = 0x00;

    /** 响应状态《调用抛出异常 */
    public static final byte STATUS_EXCEPTION = 0x03;

    @Override
    public byte[] encode(Transport transport, Object message) throws IOException {
        byte[] bytes = null;
        if(message instanceof Request){
            Request request = (Request)message;
            bytes = encodeRequest(transport,request);
        }else{
            Response response = (Response)message;
            bytes = encodeResponse(transport,response);
        }
        return bytes;
    }

    @Override
    public Object decode(Transport transport, TransportBuffer buffer) throws IOException {
        if (buffer.readableBytes() < HEAD_LENGTH) {
            return null;
        }
        if(buffer.getShort(0) != MAGIC){
            throw new LunaRpcException("not support message magic");
        }
        int bodyLength = buffer.getInt(12);
        if(buffer.readableBytes() < HEAD_LENGTH + bodyLength){
            return null;
        }

        buffer.readBytes(HEAD_LENGTH);

        byte flag = buffer.getByte(2);
        if( (flag & FLAG_RESPONSE) == 0){
            long messageId = buffer.getLong(4);
            Request request = new Request(messageId);
            if( (flag & FLAG_ONEWAY) != 0){
                request.setOneway(true);
            }
            if( (flag & FLAG_HEARTBEAT) != 0 ){
                request.setHeartbeat(true);
            }else{
                byte[] body = new byte[bodyLength];
                buffer.getBytes(HEAD_LENGTH,body,0,bodyLength);
                try {
                    Invocation invocation = decodeRequestBody(transport,body);
                    request.setData(invocation);
                } catch (ClassNotFoundException e) {
                    throw new LunaRpcException("Class not find",e);
                }
            }

            buffer.readBytes(bodyLength);

            return request;
        }else{
            long messageId = buffer.getLong(4);
            Response response = new Response(messageId);
            if( (flag & FLAG_HEARTBEAT) != 0 ){
                response.setHeartbeat(true);
            }else{
                byte status = buffer.getByte(3);
                byte[] body = new byte[bodyLength];
                buffer.getBytes(HEAD_LENGTH,body,0,bodyLength);
                try{
                    Object result = decodeResponseBody(transport,body);
                    if( status == STATUS_OK){
                        response.setValue(result);
                    }else{
                        response.setException((Exception)result);
                    }
                }catch (ClassNotFoundException e){
                    throw new LunaRpcException("Class not find",e);
                }
            }

            buffer.readBytes(bodyLength);

            return response;
        }
    }

    private byte[] encodeRequest(Transport transport, Request request) throws IOException {
        byte[] body = null;

        if(request.isHeartbeat()){
            body = new byte[0];
        }else{
            URL url = transport.getUrl();
            Invocation invocation = (Invocation) request.getData();
            String serializationName = transport.getUrl().getParameter(URLParamType.serialize.name());
            Serialization serialization = ExtensionLoader.getExtension(
                    Serialization.class,
                    serializationName != null ? serializationName : URLParamType.serialize.getValue()
            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutput output = createOutput(outputStream);
            output.writeUTF(url.getApplication());
            output.writeUTF(url.getService());
            output.writeUTF(url.getVersion());
            output.writeUTF(invocation.getMethodName());
            output.writeUTF(ReflectUtil.getMethodParamDesc(invocation.getParameterTypes()));
            for(Object o : invocation.getArguments()){
                output.writeObject(serialization.serialize(o));
            }
            output.writeInt(invocation.getAttachments().size());
            for(Map.Entry<String,String> entry : invocation.getAttachments().entrySet()){
                output.writeUTF(entry.getKey());
                output.writeUTF(entry.getValue());
            }

            output.close();

            body = outputStream.toByteArray();
        }
        int messageLength = HEAD_LENGTH + body.length;
        byte[] message = new byte[messageLength];
        int offset = 0;

        byte flag = FLAG_REQUEST;
        if(request.isOneway()){
            flag += FLAG_ONEWAY;
        }
        if(request.isHeartbeat()){
            flag += FLAG_HEARTBEAT;
        }

        ByteUtil.short2bytes(MAGIC,message,offset);
        offset += 2;

        message[offset] = flag;
        offset ++;

        offset ++;  //skip status

        ByteUtil.long2bytes(request.getMessageId(),message,offset);
        offset += 8;

        ByteUtil.int2bytes(body.length,message,offset);
        offset += 4;

        System.arraycopy(body,0,message,offset,body.length);
        return message;
    }

    private byte[] encodeResponse(Transport transport,Response response) throws IOException {
        byte[] body = null;
        if(response.isHeartbeat()){
            body = new byte[0];
        }else{
            String serializationName = transport.getUrl().getParameter(URLParamType.serialize.name());
            Serialization serialization = ExtensionLoader.getExtension(
                    Serialization.class,
                    serializationName != null ? serializationName : URLParamType.serialize.getValue()
            );
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutput output = createOutput(outputStream);
            output.writeUTF(response.getValue().getClass().getName());
            output.writeObject(serialization.serialize(response.getValue()));
            output.close();

            body = outputStream.toByteArray();
        }

        int messageLength = HEAD_LENGTH + body.length;
        byte[] message = new byte[messageLength];
        int offset = 0;

        byte flag = FLAG_RESPONSE;
        if(response.isHeartbeat()){
            flag += FLAG_HEARTBEAT;
        }

        byte status = STATUS_OK;
        if(response.getException() != null){
            status = STATUS_EXCEPTION;
        }

        ByteUtil.short2bytes(MAGIC,message,offset);
        offset += 2;

        message[offset] = flag;
        offset ++;

        message[offset] = status;
        offset ++;

        ByteUtil.long2bytes(response.getMessageId(),message,offset);
        offset += 8;

        ByteUtil.int2bytes(body.length,message,offset);
        offset += 4;

        System.arraycopy(body,0,message,offset,body.length);
        return message;
    }

    private Invocation decodeRequestBody(Transport transport, byte[] body) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        ObjectInput input = createInput(inputStream);

        String application = input.readUTF();
        String serviceName = input.readUTF();
        String version = input.readUTF();
        String methodName= input.readUTF();
        String paramtersDesc = input.readUTF();

        DefaultRpcInvocation invocation = new DefaultRpcInvocation();
        String serializationName = transport.getUrl().getParameter(URLParamType.serialize.name());
        Serialization serialization = ExtensionLoader.getExtension(
                Serialization.class,
                serializationName != null ? serializationName : URLParamType.serialize.getValue()
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

    private Object decodeResponseBody(Transport transport, byte[] body) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        ObjectInput input = createInput(inputStream);

        String serializationName = transport.getUrl().getParameter(URLParamType.serialize.name());
        Serialization serialization = ExtensionLoader.getExtension(
                Serialization.class,
                serializationName != null ? serializationName : URLParamType.serialize.getValue()
        );

        String className = input.readUTF();
        Class<?> clz = ReflectUtil.forName(className);

        Object result = serialization.deserialize((byte[])input.readObject(),clz);
        return result;
    }

    private ObjectInput createInput(InputStream in) {
        try {
            return new ObjectInputStream(in);
        } catch (Exception e) {
            throw new LunaRpcException(this.getClass().getSimpleName() + " createInput error", e);
        }
    }

    private ObjectOutput createOutput(OutputStream out){
        try{
            return new ObjectOutputStream(out);
        }catch (Exception e){
            throw new LunaRpcException(this.getClass().getSimpleName() + " createOutput error",e);
        }
    }

    private Map<String, String> decodeRequestAttachments(ObjectInput input) throws IOException, ClassNotFoundException {
        int size = input.readInt();

        Map<String, String> attachments = new HashMap<String, String>();

        for (int i = 0; i < size; i++) {
            attachments.put(input.readUTF(), input.readUTF());
        }

        return attachments;
    }

}
