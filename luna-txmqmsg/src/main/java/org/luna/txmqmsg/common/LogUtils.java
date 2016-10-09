package org.luna.txmqmsg.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 * Created by luliru on 2016/10/8.
 */
public class LogUtils {

    private static Logger log = LoggerFactory.getLogger(Constraint.LOG_NAME);

    public static void error(String msg, Throwable t){
        log.error(msg,t);
    }

    private LogUtils(){
    }
}
