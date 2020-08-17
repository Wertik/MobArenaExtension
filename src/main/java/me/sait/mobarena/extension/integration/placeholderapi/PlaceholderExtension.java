package me.sait.mobarena.extension.integration.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sait.mobarena.extension.MobArenaExtensionPlugin;
import me.sait.mobarena.extension.extension.Extension;

public class PlaceholderExtension extends Extension {

    private PlaceholderExpansion expansion;

    @Override
    public String getName() {
        return "placeholderapi";
    }

    @Override
    public String getPluginName() {
        return "PlaceholderAPI";
    }

    @Override
    public boolean onEnable() {
        this.expansion = new MobArenaExpansion(MobArenaExtensionPlugin.getInstance().getMobArena());
        return this.expansion.register();
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onDisable() {
        this.expansion.unregister();
    }
}