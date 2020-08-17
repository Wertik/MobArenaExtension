package me.sait.mobarena.extension.integration.mythicmob.listeners;

import com.garbagemule.MobArena.events.ArenaEndEvent;
import me.sait.mobarena.extension.integration.mythicmob.MythicMobsExtension;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MobArenaListener implements Listener {

    private final MythicMobsExtension extension;

    public MobArenaListener(MythicMobsExtension extension) {
        this.extension = extension;
    }

    @EventHandler(ignoreCancelled = true)
    public void onArenaEnd(ArenaEndEvent event) {
        extension.arenaEnd(event.getArena());
    }
}