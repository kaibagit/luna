package org.luna.rpc.filter;

import org.luna.rpc.core.Caller;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.Result;

/**
 * Created by luliru on 2016/11/4.
 */
public interface Filter {

    public Result filter(Caller<?> caller, Invocation invocation);

    public int getOrder();

    public boolean filterInvoker();

    public boolean filterClient();

}
