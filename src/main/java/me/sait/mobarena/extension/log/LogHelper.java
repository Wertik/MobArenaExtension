package me.sait.mobarena.extension.log;

import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.config.ConfigManager;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class LogHelper {

    public static LogLevel defaultLevel = LogLevel.USEFUL;

    public static void debug(String message) {
        log(message, LogLevel.DEBUG);
    }

    public static void info(String message) {
        log(message, LogLevel.USEFUL);
    }

    public static void warn(String message) {
        log(message, LogLevel.WARNING);
    }

    public static void error(String message) {
        log(message, LogLevel.ERROR);
    }

    public static void log(Object obj) {
        log(String.valueOf(obj));
    }

    public static void log(Exception e) {
        log(ExceptionUtils.getStackTrace(e));
    }

    public static void log(String message) {
        log(message, defaultLevel);
    }

    public static void log(Object obj, LogLevel level) {
        log(String.valueOf(obj), level);
    }

    public static void log(String message, LogLevel level) {

        if (level == null) level = defaultLevel;

        int levelSetting = ConfigManager.isInitialized() ? ConfigManager.getLogLevel() : defaultLevel.ordinal();

        if (levelSetting > LogLevel.getLowestPriority().ordinal()) {
            levelSetting = LogLevel.getLowestPriority().ordinal();
        }

        if (level.ordinal() < levelSetting) {
            return;
        }

        if (level.ordinal() >= LogLevel.ERROR.ordinal()) {
            getLog().severe(message);
        } else if (level.ordinal() >= LogLevel.WARNING.ordinal()) {
            getLog().warning(message);
        } else {
            getLog().info(message);
        }
    }

    private static Logger getLog() {
        MobArenaExtension extension = MobArenaExtension.getPlugin();
        return extension != null ? extension.getLogger() : Bukkit.getLogger();
    }
}
