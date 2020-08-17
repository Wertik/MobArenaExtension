package me.sait.mobarena.extension;

import com.garbagemule.MobArena.MobArena;
import me.sait.mobarena.extension.commands.MobArenaExtensionCommand;
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

        loadConfig();

        LogHelper.load();

        setupMobArena();

        extensionManager = new ExtensionManager(this);

        new PlaceholderExtension().register();
        new MythicMobsExtension().register();
        new DiscordSRVExtension().register();

        getCommand("mobarenaextension").setExecutor(new MobArenaExtensionCommand(this));

        startMetrics();
    }

    @Override
    public void onDisable() {
        extensionManager.disable();
    }

    public void reload(CommandSender sender) {

        long start = System.currentTimeMillis();

        setupMobArena();

        loadConfig();
        LogHelper.load();
        extensionManager.reload();

        sender.sendMessage(CommonUtils.color("&7Done... reload took &f" + (System.currentTimeMillis() - start) + "&7ms."));
    }

    public void loadConfig() {
        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try {
                saveResource("config.yml", false);
            } catch (IllegalArgumentException ignored) {
            }
        }

        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    private void setupMobArena() {
        this.mobArena = (MobArena) getServer().getPluginManager().getPlugin("MobArena");

        if (this.mobArena == null) {
            getExtensionManager().disable();
            LogHelper.error("Mob Arena is not installed, install and reload this plugin.");
        }
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