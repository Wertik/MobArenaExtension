package me.sait.mobarena.extension.extensions;

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

    /**
     * Get the name of the extension.
     */
    public abstract String getName();

    /**
     * Name of a required plugin. ExtensionManager checks if it's enabled automatically.
     *
     * @return name of a required plugin, return null to disable the check.
     */
    public abstract String getPluginName();

    /**
     * Called when the extension is enabled.
     */
    public abstract boolean onEnable();

    /**
     * Called when the extension is reloaded.
     */
    public abstract void onReload();

    /**
     * Called when the extension is disabled.
     */
    public abstract void onDisable();

    /**
     * Attempt to enable the configuration.
     * Will fail if: MobArena is not installed, plugin specified in #getPluginName() is not installed
     * or this extension returning false #onEnable().
     */
    public boolean enable() {
        this.enabled = plugin.getExtensionManager().enableExtension(this);
        return this.enabled;
    }

    /**
     * Reload the extension.
     * Also reloads the configuration beforehand.
     */
    public void reload() {
        plugin.getExtensionManager().reloadExtension(this);
    }

    /**
     * Disable the extension.
     */
    public void disable() {
        this.enabled = !plugin.getExtensionManager().disableExtension(this);
    }

    /**
     * Register the extension and try to enable it.
     */
    public boolean register() {
        return plugin.getExtensionManager().registerExtension(this);
    }

    /**
     * Disable and unregister the extension.
     */
    public void unregister() {
        plugin.getExtensionManager().unregisterExtension(this);
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public ConfigurationSection getConfigurationSection() {
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