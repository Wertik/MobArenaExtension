package me.sait.mobarena.extension.extension;

import com.garbagemule.MobArena.MobArena;
import me.sait.mobarena.extension.MobArenaExtensionPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class Extension {

    private final MobArenaExtensionPlugin plugin;

    private final MobArena mobArena;

    private boolean enabled = false;

    public Extension() {
        this.plugin = MobArenaExtensionPlugin.getInstance();
        this.mobArena = plugin.getMobArena();
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

    public MobArenaExtensionPlugin getExtensionPlugin() {
        return plugin;
    }

    public MobArena getMobArena() {
        return mobArena;
    }
}