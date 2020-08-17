package me.sait.mobarena.extension.integration.discordsrv.listeners;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;
import me.sait.mobarena.extension.integration.discordsrv.DiscordSrvSupport;

public class DiscordSrvListener {

    private final DiscordSrvSupport extension;

    public DiscordSrvListener(DiscordSrvSupport extension) {
        this.extension = extension;
    }

    @Subscribe
    public void onMinecraftMessagePreProcess(GameChatMessagePreProcessEvent event) {
        if (!event.isCancelled() && extension.inIsolatedChatArena(event.getPlayer()))
            event.setCancelled(true);
    }
}