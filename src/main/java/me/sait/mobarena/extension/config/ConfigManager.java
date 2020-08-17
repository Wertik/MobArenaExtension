package me.sait.mobarena.extension.config;

import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private static MobArenaExtension plugin;
    private static FileConfiguration fileConfig;

    private static boolean initialized;

    private static final String mythicMobPrefix = "mythicmob";
    private static final String placeholderAPIPrefix = "placeholderapi";
    private static final String discordSrvPrefix = "discordsrv";
    private static final String enableSetting = ".enable";

    public ConfigManager(MobArenaExtension plugin) {
        ConfigManager.plugin = plugin;
        ConfigManager.fileConfig = plugin.getConfig();

        initialized = true;
    }

    public void reload() {
        fileConfig = plugin.getConfig();
    }

    public static boolean isInitialized() {
        return initialized;
    }

    // General

    public static int getLogLevel() {
        if (!initialized) return LogLevel.USEFUL.ordinal();
        return generalSettings().getInt("log-level", LogHelper.defaultLevel.ordinal());
    }

    // Extension

    public static boolean isMythicMobEnabled() {
        if (!initialized) return false;
        return extensionSettings().getBoolean(mythicMobPrefix + enableSetting, false);
    }

    public static boolean isBlockNonArenaMythicMob() {
        if (!initialized) return false;
        return extensionSettings().getBoolean(mythicMobPrefix + ".block-non-arena-mythic-mob", true);
    }

    public static boolean isPlaceholderAPIEnabled() {
        if (!initialized) return false;
        return extensionSettings().getBoolean(placeholderAPIPrefix + enableSetting, false);
    }

    public static boolean isDiscordSrvEnabled() {
        if (!initialized) return false;
        return extensionSettings().getBoolean(discordSrvPrefix + enableSetting, false);
    }

    // Section

    private static ConfigurationSection generalSettings() {
        return fileConfig.getConfigurationSection("general");
    }

    private static ConfigurationSection extensionSettings() {
        return fileConfig.getConfigurationSection("extension");
    }
}