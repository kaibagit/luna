package org.luna.rpc.core.buildin;

import java.util.HashMap;
import java.util.Map;

import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.Invoker;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.core.URL;
import org.luna.rpc.transport.MessageHandler;
import org.luna.rpc.transport.Transport;
import org.luna.rpc.util.LunaRpcUtil;

/**
 * Created by luliru on 2016/11/12.
 */
public class DefaultMessageHandler implements MessageHandler {

    private Map<String, Invoker<?>> invokers = new HashMap<>();

    @Override
    public Object handle(Transport transport, Object message) {
        if (transport == null || message == null) {
            throw new LunaRpcException("DefaultMessageHandler handler(channel, message) params is null");
        }

        if (!(message instanceof Invocation)) {
            throw new LunaRpcException("DefaultMessageHandler message type not support: " + message.getClass());
        }

        Invocation invocation = (Invocation) message;
        String serviceDesc = LunaRpcUtil.getUniqueServiceDecription(invocation);
        Invoker invoker = invokers.get(serviceDesc);
        return invoker.call(invocation);
    }

    public synchronized void addInvoker(Invoker<?> invoker) {
        URL url = invoker.getUrl();
        String serviceDesc = LunaRpcUtil.getUniqueServiceDecription(url);
        invokers.put(serviceDesc,invoker);
    }
}
