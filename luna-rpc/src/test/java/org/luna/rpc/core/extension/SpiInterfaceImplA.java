package org.luna.rpc.core.extension;

/**
 * Created by luliru on 2017/3/10.
 */
@Spi(name="A")
public class SpiInterfaceImplA implements SpiInterface {

    @Override
    public String getName() {
        return "A";
    }
}
