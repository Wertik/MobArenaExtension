package me.sait.mobarena.extension.extensions.internal.discordsrv;

import com.garbagemule.MobArena.framework.Arena;
import github.scarsz.discordsrv.DiscordSRV;
import me.sait.mobarena.extension.extensions.Extension;
import me.sait.mobarena.extension.extensions.internal.discordsrv.listeners.DiscordSRVListener;
import org.bukkit.entity.Player;

public class DiscordSRVExtension extends Extension {

    private DiscordSRVListener listener;

    @Override
    public String getName() {
        return "discordsrv";
    }

    @Override
    public String getPluginName() {
        return "DiscordSRV";
    }

    @Override
    public boolean onEnable() {
        registerListeners();
        return true;
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onDisable() {
        unregisterListeners();
    }

    public boolean inIsolatedChatArena(Player player) {
        Arena arena = getMobArena().getArenaMaster().getArenaWithPlayer(player);
        return arena != null && arena.hasIsolatedChat();
    }

    private void registerListeners() {
        if (listener == null)
            listener = new DiscordSRVListener(this);
        DiscordSRV.api.subscribe(listener);
    }

    private void unregisterListeners() {
        if (listener != null) {
            DiscordSRV.api.unsubscribe(listener);
            listener = null;
        }
    }
}