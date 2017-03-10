package org.luna.rpc.core.extension;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by luliru on 2017/3/10.
 */
public class ExtensionLoaderTest {

    @Test
    public void testGetExtension(){
        SpiInterface spi_a1 = ExtensionLoader.getExtension(SpiInterface.class,"A");
        SpiInterface spi_a2 = ExtensionLoader.getExtension(SpiInterface.class,"A");
        SpiInterface spi_a3 = ExtensionLoader.getExtension(SpiInterface.class,"A",false);
        Assert.assertSame(spi_a1,spi_a2);
        Assert.assertNotSame(spi_a1,spi_a3);

        SpiInterface spi_b = ExtensionLoader.getExtension(SpiInterface.class,"B");
        Assert.assertEquals("A",spi_a1.getName());
        Assert.assertEquals("B",spi_b.getName());
    }
}
