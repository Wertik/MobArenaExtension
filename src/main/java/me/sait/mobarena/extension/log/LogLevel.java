package me.sait.mobarena.extension.log;

public enum LogLevel {

    DEBUG,
    DETAIL,
    USEFUL,
    WARNING,
    ERROR,
    CRITICAL;

    public static LogLevel lowest() {
        return LogLevel.CRITICAL;
    }
}