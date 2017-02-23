package org.luna.rpc.filter.buildin;

import org.luna.rpc.core.Caller;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.Result;
import org.luna.rpc.core.extension.Spi;
import org.luna.rpc.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 访问日志Filter
 * Created by luliru on 2017/2/23.
 */
@Spi
public class AccessLogFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(AccessLogFilter.class);

    @Override
    public Result filter(Caller<?> caller, Invocation invocation) {
        String msg = String.format("Received invocation => group=%s,service=%s,method=%s,version=%s",
                invocation.getGroup(),invocation.getServiceName(),invocation.getMethodName(),invocation.getVersion());
        logger.info(msg);
        return caller.call(invocation);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean filterInvoker() {
        return true;
    }

    @Override
    public boolean filterClient() {
        return false;
    }

}
