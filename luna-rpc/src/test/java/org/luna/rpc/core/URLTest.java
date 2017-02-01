package org.luna.rpc.core;

import org.junit.Test;
import org.luna.rpc.common.constant.URLParamType;

import static org.junit.Assert.assertEquals;

/**
 * Created by kaiba on 2017/2/1.
 */
public class URLTest {

    @Test
    public void testToFullStr(){
        URL url = new URL("luna","192.168.1.1",8080,"default_group","com.luna.rpc.example.DemoService","1.0");
        url.addParameter(URLParamType.side.getName(),"provider");
        assertEquals("luna://192.168.1.1:8080/com.luna.rpc.example.DemoService?side=provider",url.toFullStr());
    }
}
