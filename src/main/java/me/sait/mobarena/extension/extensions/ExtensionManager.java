package me.sait.mobarena.extension.extensions;

import me.sait.mobarena.extension.MobArenaExtensionPlugin;
import me.sait.mobarena.extension.log.LogHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExtensionManager {

    private final MobArenaExtensionPlugin plugin;

    private final Map<String, Extension> registeredExtensions = new HashMap<>();

    public ExtensionManager(MobArenaExtensionPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean disableExtension(Extension extension) {

        if (!extension.isEnabled()) return false;

        try {
            extension.onDisable();
            LogHelper.debug("Disabled " + extension.getName());
            return true;
        } catch (Exception e) {
            LogHelper.info("Encountered an error when enabling " + extension.getName());
            LogHelper.error("Error: " + e.getMessage());

            LogHelper.debug(e.toString());
            return false;
        }
    }

    public void reloadExtension(Extension extension) {

        if (plugin.getMobArena() == null) {
            LogHelper.debug("Could not reload extension " + extension.getName() + ", Mob Arena not installed -- disabling.");
            disableExtension(extension);
            return;
        }

        if (!extension.isEnabled() && shouldEnable(extension.getName())) {
            enableExtension(extension);
            return;
        }

        if (extension.getPluginName() != null && !checkDependency(extension.getPluginName())) {
            LogHelper.debug("Extension " + extension.getName() + " not reloaded, dependency " + extension.getPluginName() + " not installed -- disabling.");
            disableExtension(extension);
            return;
        }

        // Reload the configuration
        plugin.loadConfig();

        try {
            extension.onReload();
            LogHelper.debug("Reloaded extension " + extension.getName());
        } catch (Exception e) {
            LogHelper.info("Encountered an error when enabling " + extension.getName() + ", trying to disable.");
            LogHelper.error("Error: " + e.getMessage());

            LogHelper.debug(e.toString());

            disableExtension(extension);
        }
    }

    public boolean enableExtension(Extension extension) {

        if (plugin.getMobArena() == null) {
            LogHelper.debug("Could not enable extension " + extension.getName() + ", Mob Arena not installed -- disabling.");
            disableExtension(extension);
            return false;
        }

        if (extension.isEnabled() || !shouldEnable(extension.getName())) {
            LogHelper.debug("Could not enable " + extension.getName() + ", enabled when it shouldn't be -- disabling.");
            disableExtension(extension);
            return false;
        }

        if (extension.getPluginName() != null && !checkDependency(extension.getPluginName())) {
            LogHelper.debug("Extension " + extension.getName() + " was not enabled, dependency not installed.");
            return false;
        }

        try {
            boolean result = extension.onEnable();
            if (result)
                LogHelper.debug("Enabled extension " + extension.getName());
            else
                LogHelper.debug("Could not enable extension " + extension.getName() + ", extension refused.");
            return result;
        } catch (Exception e) {
            LogHelper.info("Encountered an error when enabling " + extension.getName() + ", trying to disable.");
            LogHelper.error("Error: " + e.getMessage());

            LogHelper.debug(e.toString());

            disableExtension(extension);
            return false;
        }
    }

    /**
     * Register an extension and try to enable it.
     */
    public boolean registerExtension(Extension extension) {
        if (extension == null)
            return false;

        if (isRegistered(extension.getName())) {
            LogHelper.debug("Could not register " + extension.getName() + ", already registered.");
            return false;
        }

        this.registeredExtensions.put(extension.getName(), extension);
        LogHelper.debug("Registered extension " + extension.getName());

        return enableExtension(extension);
    }

    /**
     * Unregister an extension.
     */
    public void unregisterExtension(String extensionName) {
        unregisterExtension(getExtension(extensionName));
    }

    public void unregisterExtension(Extension extension) {
        if (extension == null) return;

        extension.disable();

        this.registeredExtensions.remove(extension.getName());
        LogHelper.debug("Unregistered extension " + extension.getName());
    }

    public void enableAll() {
        for (Extension extension : this.registeredExtensions.values()) {
            extension.enable();
        }
    }

    public void reloadAll() {
        for (Extension extension : this.registeredExtensions.values()) {
            extension.reload();
        }
    }

    /**
     * Disable all registered extensions.
     */
    public void disableAll() {
        for (Extension extension : this.registeredExtensions.values()) {
            extension.disable();
        }
    }

    private boolean checkDependency(String dependencyName) {
        return plugin.getServer().getPluginManager().getPlugin(dependencyName) != null;
    }

    private boolean shouldEnable(String extensionName) {
        return plugin.getConfig().getBoolean("extensions." + extensionName + ".enabled", false);
    }

    public boolean isRegistered(String extensionName) {
        return this.registeredExtensions.containsKey(extensionName);
    }

    public Extension getExtension(String extensionName) {
        return this.registeredExtensions.getOrDefault(extensionName, null);
    }

    public Set<Extension> getExtensions() {
        return new HashSet<>(this.registeredExtensions.values());
    }
}