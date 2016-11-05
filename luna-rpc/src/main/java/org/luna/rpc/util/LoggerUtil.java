package org.luna.rpc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 * Created by luliru on 2016/11/5.
 */
public class LoggerUtil {

    private static Logger error = LoggerFactory.getLogger("error");

    public static void error(String msg) {
        error.error(msg);
    }

    public static void error(String format, Object... argArray) {
        error.error(format, argArray);
    }

    public static void error(String msg, Throwable t) {
        error.error(msg, t);
    }
}
