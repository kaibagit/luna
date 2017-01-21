package org.luna.rpc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 * Created by luliru on 2016/11/5.
 */
public class LoggerUtil {

    private static Logger logger = LoggerFactory.getLogger("luna-rpc");

    public static void error(String msg) {
        logger.error(msg);
    }

    public static void error(String format, Object... argArray) {
        logger.error(format, argArray);
    }

    public static void error(String msg, Throwable t) {
        logger.error(msg, t);
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void warn(String msg) {
        logger.warn(msg);
    }

    public static void debug(String format, Object arg){
        logger.debug(format,arg);
    }

    public static void debug(String format, Object... arguments){
        logger.debug(format,arguments);
    }

    public static void debug(String msg, Throwable t){
        logger.debug(msg,t);
    }
}
