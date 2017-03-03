package org.luna.rpc.transport.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.luna.rpc.core.URL;

/**
 * Created by luliru on 2017/3/2.
 */
public class NettyChannelFactory extends BasePoolableObjectFactory {

    private URL url;

    private Bootstrap bootstrap;

    public NettyChannelFactory(URL url, Bootstrap bootstrap){
        this.url = url;
        this.bootstrap = bootstrap;
    }

    @Override
    public Object makeObject() throws Exception {
        Channel channel = bootstrap.connect(url.getHost(), url.getPort()).sync().channel();
        return channel;
    }
}
