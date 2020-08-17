package me.sait.mobarena.extension.integration.discordsrv;

import com.garbagemule.MobArena.framework.Arena;
import github.scarsz.discordsrv.DiscordSRV;
import me.sait.mobarena.extension.extension.Extension;
import me.sait.mobarena.extension.integration.discordsrv.listeners.DiscordSrvListener;
import org.bukkit.entity.Player;

public class DiscordSrvSupport extends Extension {

    private DiscordSrvListener discordSrvListener;

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
        if (discordSrvListener == null) {
            discordSrvListener = new DiscordSrvListener(this);
        }
        DiscordSRV.api.subscribe(discordSrvListener);
    }

    private void unregisterListeners() {
        if (discordSrvListener != null) {
            DiscordSRV.api.unsubscribe(discordSrvListener);
            discordSrvListener = null;
        }
    }
}