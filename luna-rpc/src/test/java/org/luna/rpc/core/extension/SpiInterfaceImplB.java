package org.luna.rpc.core.extension;

/**
 * Created by luliru on 2017/3/10.
 */
@Spi(name="B")
public class SpiInterfaceImplB implements SpiInterface{
    @Override
    public String getName() {
        return "B";
    }
}
