package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.waves.MACreature;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import me.sait.mobarena.extension.MobArenaExtensionPlugin;
import me.sait.mobarena.extension.extension.Extension;
import me.sait.mobarena.extension.integration.mythicmob.listeners.MobArenaListener;
import me.sait.mobarena.extension.integration.mythicmob.listeners.MythicMobListener;
import me.sait.mobarena.extension.log.LogHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.*;

public class MythicMobsSupport implements Extension {

    public static final String PLUGIN_NAME = "MythicMobs";

    private final MobArenaExtensionPlugin extension;
    private final MobArena mobArena;

    private final Map<Arena, List<Entity>> cachedMythicMobs = new HashMap<>();
    private final List<MythicMob> registeredMobs = new ArrayList<>();

    public MythicMobsSupport(MobArenaExtensionPlugin extension, MobArena mobArena) {
        this.extension = extension;
        this.mobArena = mobArena;
    }

    @Override
    public void initialize() {
        registerMobs();
        registerListeners();
    }

    @Override
    public void reload() {
    }

    @Override
    public void disable() {
    }

    public void arenaSpawnMythicMob(Arena arena, Entity entity) {
        if (!cachedMythicMobs.containsKey(arena) || cachedMythicMobs.get(arena) == null) {
            cachedMythicMobs.put(arena, new ArrayList());
        }

        cachedMythicMobs.get(arena).add(entity);
        LogHelper.debug("A mythic mob spawned inside mob arena: " + entity.getCustomName());
    }

    public void arenaEnd(Arena arena) {
        cachedMythicMobs.remove(arena);
    }

    public boolean isInArena(Entity entity) {
        for (List<Entity> entities : cachedMythicMobs.values()) {
            if (entities.contains(entity)) {
                return true;
            }
        }
        return false;
    }

    public Arena getInArena(Entity entity) {
        for (Map.Entry<Arena, List<Entity>> entry : cachedMythicMobs.entrySet()) {
            if (entry.getValue().contains(entity)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Arena getArenaAtLocation(Location location) {
        return mobArena.getArenaMaster().getArenaAtLocation(location);
    }

    public void runTask(Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLater(extension, task, delay);
    }

    private void registerListeners() {
        mobArena.getServer().getPluginManager().registerEvents(new MythicMobListener(this), extension);
        mobArena.getServer().getPluginManager().registerEvents(new MobArenaListener(this), extension);
    }

    private void registerMobs() {
        Collection<MythicMob> mmMobs = MythicMobs.inst().getMobManager().getMobTypes();
        for (MythicMob mob : mmMobs) {
            if (MACreature.fromString(mob.getInternalName()) != null) {
                throw new IllegalArgumentException("Can not register mythic mobs with similar name: " + mob.getInternalName());
            }
            new MythicMobCreature(this, mob);
            registeredMobs.add(mob);
            LogHelper.debug("Registered mythic mob: " + mob.getInternalName());
        }
    }
}
