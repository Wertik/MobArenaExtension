package me.sait.mobarena.extension;

import com.garbagemule.MobArena.MobArena;
import me.sait.mobarena.extension.api.Integration;
import me.sait.mobarena.extension.config.ConfigManager;
import me.sait.mobarena.extension.config.Constants;
import me.sait.mobarena.extension.integration.discordsrv.DiscordSrvSupport;
import me.sait.mobarena.extension.integration.mythicmob.MythicMobsSupport;
import me.sait.mobarena.extension.integration.placeholderapi.PlaceholderAPISupport;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import me.sait.mobarena.extension.services.MetricsService;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class MobArenaExtension extends JavaPlugin {

    private ConfigManager configManager;
    private MobArena mobArena;
    private MetricsService metricsService;

    private static List<Integration> extensions;

    private MythicMobsSupport mythicMobsSupport;
    private PlaceholderAPISupport placeholderAPISupport;
    private DiscordSrvSupport discordSrvSupport;

    public static MobArenaExtension getPlugin() {
        return getPlugin(MobArenaExtension.class);
    }

    @Override
    public void onEnable() {
        if (extensions == null) {
            extensions = new ArrayList<>();
        }

        setupConfig();

        initMobArena();
        initMythicMob();
        initPlaceholderAPI();
        initDiscordSrv();

        startServices();
    }

    @Override
    public void onDisable() {
        //TODO - graceful disable all modules
        disableDiscordSrv();
    }

    public static boolean registerExtension(Integration extension) {
        if (extensions == null) {
            extensions = new ArrayList<>();
        }
        try {
            if (extension == null) return false;

            extension.onEnable();
        } catch (Exception e) {
            LogHelper.log(e);
            try {
                extension.onDisable();
            } catch (Exception ex) {
                LogHelper.log(e);
            }
            return false;
        }
        extensions.add(extension);
        return true;
    }

    private void setupConfig() {
        loadDefaultConfig();
        configManager = new ConfigManager(this);
    }

    private void loadDefaultConfig() {
        boolean pluginFolderNotExists = !getDataFolder().exists();
        File file = new File(getDataFolder(), "config.yml");

        if (pluginFolderNotExists || !file.exists()) {
            LogHelper.info("config.yml not found, creating new one!");
            saveDefaultConfig();
        }
    }

    private void reload() {
        loadDefaultConfig();
        reloadConfig();
        configManager.reload();
    }

    private void startServices() {
        metricsService = new MetricsService();
        metricsService.start();
    }

    private void initMobArena() {
        mobArena = (MobArena) getServer().getPluginManager().getPlugin(Constants.MOB_ARENA_PLUGIN_NAME);
        if (mobArena == null || !mobArena.isEnabled()) {
            throw new NullPointerException("This extension requires core plugin MobArena installed and enabled");
        }
    }

    private void initMythicMob() {
        if (configManager.isMythicMobEnabled()) {
            LogHelper.log("Init mythic mob", LogLevel.DETAIL);
            if (!getServer().getPluginManager().isPluginEnabled(MythicMobsSupport.PLUGIN_NAME)) {
                LogHelper.log(
                        "MythicMobs plugin can not be found. Install it or disable mythicmob extension in config",
                        LogLevel.CRITICAL
                );
                getServer().getPluginManager().disablePlugin(this);
            }

            mythicMobsSupport = new MythicMobsSupport(this, mobArena);
            mythicMobsSupport.onEnable();
            extensions.add(mythicMobsSupport);

            try {
                if (mobArena.getLastFailureCause() != null) {
                    LogHelper.debug("Auto reload MobArena so it load mythic mobs now if have");
                    mobArena.reload();
                }
            } catch (RuntimeException error) {
            }
        }
    }

    private void initPlaceholderAPI() {
        if (configManager.isPlaceholderAPIEnabled()) {
            LogHelper.log("Init placeholder api", LogLevel.DETAIL);
            if (!getServer().getPluginManager().isPluginEnabled(PlaceholderAPISupport.PLUGIN_NAME)) {
                LogHelper.log(
                        "PlaceholderAPI plugin can not be found. Install it or disable placeholderapi extension in config",
                        LogLevel.CRITICAL
                );
                getServer().getPluginManager().disablePlugin(this);
            }

            placeholderAPISupport = new PlaceholderAPISupport(this, mobArena);
            placeholderAPISupport.onEnable();
            extensions.add(placeholderAPISupport);
        }
    }

    private void initDiscordSrv() {
        if (ConfigManager.isDiscordSrvEnabled()) {
            LogHelper.log("Init discordsrv", LogLevel.DETAIL);
            if (!getServer().getPluginManager().isPluginEnabled(DiscordSrvSupport.PLUGIN_NAME)) {
                LogHelper.log(
                        "DiscordSRV plugin can not be found. Install it or disable discordsrv extension in config",
                        LogLevel.CRITICAL
                );
                getServer().getPluginManager().disablePlugin(this);
            }

            discordSrvSupport = new DiscordSrvSupport(mobArena);
            discordSrvSupport.onEnable();
            extensions.add(discordSrvSupport);
        }
    }

    private void disableDiscordSrv() {
        if (configManager.isDiscordSrvEnabled() &&
                getServer().getPluginManager().isPluginEnabled(DiscordSrvSupport.PLUGIN_NAME) &&
                discordSrvSupport != null
        ) {
            discordSrvSupport.onDisable();
            extensions.remove(discordSrvSupport);
        }
    }
}
