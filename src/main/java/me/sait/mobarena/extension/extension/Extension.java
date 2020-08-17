package me.sait.mobarena.extension.extension;

import me.sait.mobarena.extension.MobArenaExtensionPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class Extension {

    private final MobArenaExtensionPlugin plugin;

    private boolean enabled = false;

    public Extension() {
        this.plugin = MobArenaExtensionPlugin.getInstance();
    }

    public abstract String getName();

    public abstract String getPluginName();

    public abstract boolean onEnable();

    public abstract void onReload();

    public abstract void onDisable();

    // Wrapper calls
    public boolean enable() {
        this.enabled = onEnable();
        return this.enabled;
    }

    public void reload() {
        onReload();
    }

    public void disable() {
        onDisable();
        this.enabled = false;
    }

    public boolean register() {
        return plugin.getExtensionManager().registerExtension(this);
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public ConfigurationSection getSection() {
        return ensureConfigurationSection("extensions." + getName());
    }

    private ConfigurationSection ensureConfigurationSection(String path) {
        return plugin.getConfig().contains(path) ? plugin.getConfig().getConfigurationSection(path) : plugin.getConfig().createSection(path);
    }

    public boolean isEnabled() {
        return enabled;
    }
}