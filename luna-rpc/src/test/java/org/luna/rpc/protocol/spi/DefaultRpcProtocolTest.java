package org.luna.rpc.protocol.spi;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.protocol.Protocol;

/**
 * Created by luliru on 2017/1/18.
 */
public class DefaultRpcProtocolTest {

    @Test
    public void testDefaultRpcProtocolLoaded(){
        Protocol protocol = ExtensionLoader.getExtension(Protocol.class,"luna");
        assertNotNull(protocol);
    }
}
