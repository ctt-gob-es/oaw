package es.inteco.common.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Logger {
    public static final int LOG_LEVEL_ERROR = 1;
    public static final int LOG_LEVEL_DEBUG = 2;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_WARNING = 4;

    private Logger() {
    }

    public static void putLog(String message, Class clazz, int logLevel) {
        Log logger = LogFactory.getLog(clazz);
        writeLog(logger, message, logLevel, null);
    }

    public static void putLog(String message, Class clazz, int logLevel, Exception ex) {
        Log logger = LogFactory.getLog(clazz);
        writeLog(logger, message, logLevel, ex);
    }

    private static void writeLog(Log logger, String message, int logLevel, Exception exception) {
        if (logLevel == LOG_LEVEL_ERROR) {
            logger.error(message, exception);
        } else if (logLevel == LOG_LEVEL_DEBUG) {
            logger.debug(message, exception);
        } else if (logLevel == LOG_LEVEL_INFO) {
            logger.info(message, exception);
        } else if (logLevel == LOG_LEVEL_WARNING) {
            logger.warn(message, exception);
        }
    }

}
