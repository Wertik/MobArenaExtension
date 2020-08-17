package me.sait.mobarena.extension.integration.discordsrv.listeners;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;
import me.sait.mobarena.extension.integration.discordsrv.DiscordSRVExtension;

public class DiscordSRVListener {

    private final DiscordSRVExtension extension;

    public DiscordSRVListener(DiscordSRVExtension extension) {
        this.extension = extension;
    }

    @Subscribe
    public void onMinecraftMessagePreProcess(GameChatMessagePreProcessEvent event) {
        if (!event.isCancelled() && extension.inIsolatedChatArena(event.getPlayer()))
            event.setCancelled(true);
    }
}