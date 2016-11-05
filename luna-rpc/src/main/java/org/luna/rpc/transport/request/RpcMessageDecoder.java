package org.luna.rpc.transport.request;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.luna.rpc.common.constant.MessageConstant;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.transport.RpcRequest;
import org.luna.rpc.transport.RpcResponse;

import java.util.List;

/**
 * Created by kaiba on 2016/5/24.
 */
public class RpcMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < MessageConstant.HEAD_LENGTH) {  //这个HEAD_LENGTH是我们用于表示头长度的字节数。
            return;
        }
        if(in.getShort(0) != MessageConstant.MAGIC){
            throw new LunaRpcException("not support message magic");
        }
        if(in.getShort(15) != MessageConstant.HEAD_LENGTH){
            throw new LunaRpcException("error message head lenght");
        }
        long bodySize = in.getLong(128);
        if(in.readableBytes() < MessageConstant.HEAD_LENGTH+bodySize){
            return;
        }
        byte flags = in.getByte(39);
        int rp = flags % 2;
        if(rp == MessageConstant.RP_REQUEST){

        }else{

        }


        byte[] body = new byte[MessageConstant.HEAD_LENGTH];
        in.readBytes(body);

        RpcMessage o = decode(body);
        out.add(o);
    }

    private RpcMessage decode(byte[] data){


        return null;
    }

    private RpcRequest decodeRequest(ByteBuf in){
        RpcRequest request = new RpcRequest();
        //skip magic and head size
        in.readLong();
        //skip version
        in.readByte();
        //get flags
        int flags = in.readByte();
        int heartBeatFlags = (flags >> 2) % 2;
        int serializeType = flags >> 3;
        //skip status code and reserved
        in.readInt();
        //get message id
        long messageId = in.readLong();

        request.setHeartBeat(heartBeatFlags == 1);
        request.setSerializeType(serializeType);
        request.setMessageId(messageId);




        return request;
    }

    private RpcResponse decodeResponse(ByteBuf in){
        RpcResponse response = new RpcResponse();
        //skip magic and head size
        in.readLong();
        //skip version
        in.readByte();
        //get flags
        int flags = in.readByte();
        int heartBeatFlags = (flags >> 2) % 2;
        int serializeType = flags >> 3;
        //get status code
        int statusCode = in.readByte();
        //skip reserved
        in.readByte();
        //get message id
        long messageId = in.readLong();

        response.setMessageId(messageId);
        response.setHeartBeat(heartBeatFlags == 1);
        response.setSerializeType(serializeType);

        return response;
    }
}
