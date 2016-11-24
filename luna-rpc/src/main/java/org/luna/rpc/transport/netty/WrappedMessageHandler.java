package org.luna.rpc.transport.netty;

import org.luna.rpc.core.Result;
import org.luna.rpc.protocol.MessageHandler;
import org.luna.rpc.transport.Request;
import org.luna.rpc.transport.Response;
import org.luna.rpc.transport.Transport;

/**
 * Created by luliru on 2016/11/21.
 */
public class WrappedMessageHandler implements MessageHandler {

    private MessageHandler messageHandler;

    public WrappedMessageHandler(MessageHandler messageHandler){
        this.messageHandler = messageHandler;
    }

    @Override
    public Object handle(Transport transport, Object message) {
        Response response = null;
        if(message instanceof Request){
            Request request = (Request) message;
            if(request.isHeartbeat()){

            }else{
                Result result = (Result) messageHandler.handle(transport,request.getData());
                response = new Response(request.getMessageId());
                response.setException(result.getException());
                response.setValue(result.getValue());
            }
        }
        return response;
    }
}
