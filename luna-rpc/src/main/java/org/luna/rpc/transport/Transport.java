package org.luna.rpc.transport;

import org.luna.rpc.core.Lifecycle;
import org.luna.rpc.core.URL;

/**
 * Created by luliru on 2016/11/12.
 */
public interface Transport extends Lifecycle {

    URL getUrl();
}
