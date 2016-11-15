package org.luna.rpc.codec;

import java.io.IOException;

import org.luna.rpc.transport.Transport;
import org.luna.rpc.transport.TransportBuffer;

/**
 * Created by luliru on 2016/11/7.
 */
public interface Codec {

    byte[] encode(Transport transport, Object message) throws IOException;

    Object decode(Transport transport, TransportBuffer buffer) throws IOException;

}
