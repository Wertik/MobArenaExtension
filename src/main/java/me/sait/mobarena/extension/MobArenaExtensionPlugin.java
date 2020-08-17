package me.sait.mobarena.extension;

import com.garbagemule.MobArena.MobArena;
import me.sait.mobarena.extension.extension.ExtensionManager;
import me.sait.mobarena.extension.integration.discordsrv.DiscordSRVExtension;
import me.sait.mobarena.extension.integration.mythicmob.MythicMobsExtension;
import me.sait.mobarena.extension.integration.placeholderapi.PlaceholderExtension;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.services.MetricsService;
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

    private MobArena mobArena;

    private ExtensionManager extensionManager;

    private FileConfiguration configuration;

    @Override
    public void onEnable() {
        instance = this;

        setupMobArena();

        loadConfig();

        extensionManager = new ExtensionManager(this);
        extensionManager.enable();

        new PlaceholderExtension().register();
        new MythicMobsExtension().register();
        new DiscordSRVExtension().register();

        startMetrics();
    }

    @Override
    public void onDisable() {
        extensionManager.disable();
    }

    public void reload(CommandSender sender) {

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

    //TODO recode, enable plugin, disable features, attempt to hook on reload.
    private void setupMobArena() {
        MobArena mobArena = (MobArena) getServer().getPluginManager().getPlugin("MobArena");

        if (mobArena == null || !mobArena.isEnabled()) {
            throw new NullPointerException("This extension requires core plugin MobArena installed and enabled");
        }

        this.mobArena = mobArena;
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

    public MobArena getMobArena() {
        return mobArena;
    }
}