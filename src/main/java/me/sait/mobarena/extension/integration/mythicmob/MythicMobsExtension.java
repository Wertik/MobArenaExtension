package me.sait.mobarena.extension.integration.mythicmob;

import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.waves.MACreature;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import me.sait.mobarena.extension.extension.Extension;
import me.sait.mobarena.extension.integration.mythicmob.listeners.MobArenaListener;
import me.sait.mobarena.extension.integration.mythicmob.listeners.MythicMobListener;
import me.sait.mobarena.extension.log.LogHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.*;

public class MythicMobsExtension extends Extension {

    private final Map<Arena, List<Entity>> cachedMythicMobs = new HashMap<>();
    private final List<MythicMob> registeredMobs = new ArrayList<>();

    @Override
    public String getName() {
        return "mythicmobs";
    }

    @Override
    public String getPluginName() {
        return "MythicMobs";
    }

    @Override
    public boolean onEnable() {
        registerMobs();
        registerListeners();

        return true;
    }

    @Override
    public void onReload() {
        //TODO Try to do something on reload if needed
    }

    @Override
    public void onDisable() {
    }

    public void spawnMythicMob(Arena arena, Entity entity) {
        if (!cachedMythicMobs.containsKey(arena) || cachedMythicMobs.get(arena) == null) {
            cachedMythicMobs.put(arena, new ArrayList<>());
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

    public Arena getArena(Entity entity) {
        for (Map.Entry<Arena, List<Entity>> entry : cachedMythicMobs.entrySet()) {
            if (entry.getValue().contains(entity)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Arena getArenaAtLocation(Location location) {
        return getMobArena().getArenaMaster().getArenaAtLocation(location);
    }

    public void runTask(Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLater(getExtensionPlugin(), task, delay);
    }

    private void registerListeners() {
        getExtensionPlugin().getServer().getPluginManager().registerEvents(new MythicMobListener(this), getExtensionPlugin());
        getExtensionPlugin().getServer().getPluginManager().registerEvents(new MobArenaListener(this), getExtensionPlugin());
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
