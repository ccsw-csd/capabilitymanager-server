package com.ccsw.capabilitymanager.common.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CapabilityLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(CapabilityLogger.class);
    public static void logDebug(String logging) {
        LOGGER.debug("\u001B[34m" + logging + "\u001B[0m");
    }
    public static void logInfo(String logging) {
        LOGGER.info("\u001b[32m" + logging + "\u001B[0m");
    }
    public static void logError(String logging) {
        LOGGER.error("\u001B[31m" + logging + "\u001B[0m");
    }

    public static void logWarning(String logging) {
        LOGGER.warn("\u001B[33m" + logging + "\u001B[0m");
    }
    public static void logTrace(String logging) {
        LOGGER.trace("\u001B[35m" + logging + "\u001B[0m");
    }
}
