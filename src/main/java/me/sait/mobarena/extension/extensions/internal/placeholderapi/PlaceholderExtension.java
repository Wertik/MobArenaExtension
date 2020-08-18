package me.sait.mobarena.extension.extensions.internal.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sait.mobarena.extension.MobArenaExtensionPlugin;
import me.sait.mobarena.extension.extensions.Extension;
import me.sait.mobarena.extension.utils.CommonUtils;

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
        this.expansion.register();
        //TODO Unregister old expansion and register new.
        return true;
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onDisable() {
        if (CommonUtils.compileVersionNumber(getExtensionPlugin().getServer().getPluginManager().getPlugin("PlaceholderAPI").getDescription().getVersion()) > 2108)
            this.expansion.unregister();
    }
}