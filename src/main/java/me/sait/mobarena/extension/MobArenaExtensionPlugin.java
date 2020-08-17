package me.sait.mobarena.extension;

import com.garbagemule.MobArena.MobArena;
import me.sait.mobarena.extension.config.ConfigManager;
import me.sait.mobarena.extension.config.Constants;
import me.sait.mobarena.extension.integration.discordsrv.DiscordSrvSupport;
import me.sait.mobarena.extension.integration.mythicmob.MythicMobsSupport;
import me.sait.mobarena.extension.integration.placeholderapi.MobArenaExpansion;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import me.sait.mobarena.extension.services.MetricsService;
import me.sait.mobarena.extension.extension.ExtensionManager;
import me.sait.mobarena.extension.utils.CommonUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class MobArenaExtensionPlugin extends JavaPlugin {

    private static MobArenaExtensionPlugin instance;

    public static MobArenaExtensionPlugin getInstance() {
        return instance;
    }

    private ExtensionManager extensionManager;

    private FileConfiguration configuration;

    @Override
    public void onEnable() {
        instance = this;

        loadConfig();

        extensionManager = new ExtensionManager(this);
        extensionManager.enable();

        startMetrics();
    }

    @Override
    public void onDisable() {
        extensionManager.disable();
    }

    private void reload(CommandSender sender) {

        long start = System.currentTimeMillis();

        loadConfig();
        extensionManager.reload();

        sender.sendMessage(CommonUtils.color("&7Done... reload took &f" + (System.currentTimeMillis() - start) + "&7ms."));
    }

    public void loadConfig() {
        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try {
                saveResource("config.yml", false);
            } catch (IllegalArgumentException e) {
                LogHelper.error("Could not save config.yml");
            }
        }

        this.configuration = YamlConfiguration.loadConfiguration(file);
        LogHelper.info("Loaded config.yml");
    }

    private void startMetrics() {
        MetricsService metricsService = new MetricsService();
        metricsService.start();
    }

    @NotNull
    @Override
    public FileConfiguration getConfig() {
        return this.configuration;
    }

    public ExtensionManager getExtensionManager() {
        return extensionManager;
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
            mythicMobsSupport.initialize();
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
            if (!getServer().getPluginManager().isPluginEnabled(MobArenaExpansion.PLUGIN_NAME)) {
                LogHelper.log(
                        "PlaceholderAPI plugin can not be found. Install it or disable placeholderapi extension in config",
                        LogLevel.CRITICAL
                );
                getServer().getPluginManager().disablePlugin(this);
            }

            placeholderAPISupport = new MobArenaExpansion(this, mobArena);
            placeholderAPISupport.initialize();
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
            discordSrvSupport.initialize();
            extensions.add(discordSrvSupport);
        }
    }

    private void disableDiscordSrv() {
        if (configManager.isDiscordSrvEnabled() &&
                getServer().getPluginManager().isPluginEnabled(DiscordSrvSupport.PLUGIN_NAME) &&
                discordSrvSupport != null
        ) {
            discordSrvSupport.disable();
            extensions.remove(discordSrvSupport);
        }
    }
}
