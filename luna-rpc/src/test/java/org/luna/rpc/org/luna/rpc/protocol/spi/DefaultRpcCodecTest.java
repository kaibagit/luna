package org.luna.rpc.org.luna.rpc.protocol.spi;

import org.junit.Test;
import org.luna.rpc.protocol.spi.DefaultRpcCodec;
import org.luna.rpc.transport.Response;

import java.io.IOException;

/**
 * Created by luliru on 2016/11/24.
 */
public class DefaultRpcCodecTest {

    @Test
    public void test() throws IOException {
        DefaultRpcCodec codec = new DefaultRpcCodec();
        Response response = new Response(1L);
        response.setValue("hello");

        byte[] bytes = codec.encode(null,response);

//        Object obj = codec.decode(null,bytes)
    }

}
