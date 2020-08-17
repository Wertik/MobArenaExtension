package me.sait.mobarena.extension.extension;

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

    /**
     * Disable all registered extensions.
     */
    public void disable() {
        for (Extension extension : this.registeredExtensions.values()) {
            disableExtension(extension);
        }
    }

    public void disableExtension(Extension extension) {

        if (!extension.isEnabled()) return;

        try {
            extension.disable();
        } catch (Exception e) {
            LogHelper.info("Encountered an error when enabling " + extension.getName());
            LogHelper.error("Error: " + e.getMessage());

            LogHelper.debug(e.toString());
        }
    }

    public void reload() {
        for (Extension extension : this.registeredExtensions.values()) {
            reloadExtension(extension);
        }
    }

    private boolean checkDependency(String dependencyName) {
        return plugin.getServer().getPluginManager().getPlugin(dependencyName) != null;
    }

    private boolean shouldEnable(String extensionName) {
        return plugin.getConfig().getBoolean("extensions." + extensionName + ".enabled", false);
    }

    public void reloadExtension(Extension extension) {

        if (!extension.isEnabled() && shouldEnable(extension.getName())) {
            enableExtension(extension);
            return;
        }

        try {
            if (extension.getPluginName() != null && !checkDependency(extension.getPluginName())) {
                LogHelper.debug("Extension " + extension.getName() + " was not reloaded, dependency not installed -- disabling.");
                disableExtension(extension);
                return;
            }

            extension.reload();
            LogHelper.debug("Reloaded extension " + extension.getName());
        } catch (Exception e) {
            LogHelper.info("Encountered an error when enabling " + extension.getName() + ", trying to disable.");
            LogHelper.error("Error: " + e.getMessage());

            LogHelper.debug(e.toString());

            disableExtension(extension);
        }
    }

    public void enable() {
        for (Extension extension : this.registeredExtensions.values()) {
            enableExtension(extension);
        }
    }

    public boolean enableExtension(Extension extension) {

        if (extension.isEnabled() || !shouldEnable(extension.getName())) {
            disableExtension(extension);
            return false;
        }

        try {
            if (extension.getPluginName() != null && !checkDependency(extension.getPluginName())) {
                LogHelper.debug("Extension " + extension.getName() + " was not enabled, dependency not installed.");
                return false;
            }

            boolean result = extension.enable();
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

    public boolean isRegistered(String extensionName) {
        return this.registeredExtensions.containsKey(extensionName);
    }

    public Extension getExtension(String extensionName) {
        return this.registeredExtensions.getOrDefault(extensionName, null);
    }

    /**
     * Unregister an extension.
     */
    public void unregisterExtension(String extensionName) {
        unregisterExtension(getExtension(extensionName));
    }

    public void unregisterExtension(Extension extension) {
        if (extension == null) return;

        disableExtension(extension);

        this.registeredExtensions.remove(extension.getName());
        LogHelper.debug("Unregistered extension " + extension.getName());
    }

    public Set<Extension> getExtensions() {
        return new HashSet<>(this.registeredExtensions.values());
    }
}