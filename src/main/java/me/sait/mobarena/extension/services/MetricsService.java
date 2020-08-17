package me.sait.mobarena.extension.services;

import me.sait.mobarena.extension.MobArenaExtensionPlugin;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.log.LogLevel;
import org.bstats.bukkit.Metrics;

public class MetricsService {

    private final MobArenaExtensionPlugin plugin;

    private Metrics metrics;

    public MetricsService() {
        plugin = MobArenaExtensionPlugin.getInstance();
    }

    public void start() {
        if (plugin == null || !plugin.isEnabled()) {
            return;
        }

        LogHelper.log("Starting Metrics", LogLevel.DETAIL);
        metrics = new Metrics(plugin);
    }
}