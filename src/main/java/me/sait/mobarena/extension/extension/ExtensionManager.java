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
            LogHelper.log(e.getMessage());
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
            if (extension.getPluginName() != null && !checkDependency(extension.getPluginName())) return;

            extension.reload();
        } catch (Exception e) {
            LogHelper.log(e.getMessage());

            disableExtension(extension);
        }
    }

    public void enable() {
        for (Extension extension : this.registeredExtensions.values()) {
            enableExtension(extension);
        }
    }

    public boolean enableExtension(Extension extension) {

        if (extension.isEnabled() && !shouldEnable(extension.getName())) {
            disableExtension(extension);
            return false;
        }

        try {
            if (extension.getPluginName() != null && !checkDependency(extension.getPluginName())) return false;

            return extension.enable();
        } catch (Exception e) {
            LogHelper.log(e.getMessage());

            disableExtension(extension);
            return false;
        }
    }

    /**
     * Register an extension.
     */
    public boolean registerExtension(Extension extension) {
        if (extension == null) return false;

        return enableExtension(extension);
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
    }

    public Set<Extension> getExtensions() {
        return new HashSet<>(this.registeredExtensions.values());
    }
}