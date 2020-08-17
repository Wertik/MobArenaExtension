package me.sait.mobarena.extension.integration.mythicmob.listeners;

import com.garbagemule.MobArena.framework.Arena;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import me.sait.mobarena.extension.integration.mythicmob.MythicMobsExtension;
import me.sait.mobarena.extension.log.LogHelper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MythicMobListener implements Listener {

    private final MythicMobsExtension extension;

    public MythicMobListener(MythicMobsExtension extension) {
        this.extension = extension;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMythicMobSpawn(MythicMobSpawnEvent event) {

        extension.runTask(() -> {

            ActiveMob activeMob = MythicMobs.inst().getAPIHelper().getMythicMobInstance(event.getEntity());

            LogHelper.debug("A mythic mob spawned, mythic: " + event.getMobType().getInternalName() +
                    ", entity: " + event.getMobType().getEntityType());

            // This is arena mob. no more checking need
            if (extension.isInArena(event.getEntity())) {
                return;
            }

            if (activeMob == null) {
                LogHelper.error("Could not find the entity object of spawned mythic mob.");
                return;
            }

            if (activeMob.getParent() != null) {

                Entity parent = activeMob.getParent().getEntity().getBukkitEntity();
                ActiveMob mythicParent = MythicMobs.inst().getAPIHelper().getMythicMobInstance(parent);

                if (mythicParent != null) {
                    LogHelper.debug(event.getMobType().getInternalName() + " spawned via skill Summon by " + mythicParent.getType().getInternalName());
                }

                if (extension.isInArena(parent)) {
                    LogHelper.debug(event.getMobType().getInternalName() + " was spawn by another mythic mob inside mob arena");
                    Arena arena = extension.getArena(parent);

                    if (event.getEntity() instanceof LivingEntity) {
                        arena.getMonsterManager().addMonster((LivingEntity) event.getEntity());
                    } else {
                        LogHelper.error(event.getMobType().getInternalName() + " is not a living entity, currently not compatible with Mob Arena");
                    }

                    extension.spawnMythicMob(arena, event.getEntity());
                    return;
                }
            }

            if (extension.getArenaAtLocation(event.getLocation()) != null) {
                LogHelper.debug("A non-arena mythic mob spawned inside arena: " + event.getMobType().getInternalName());

                if (extension.getConfig().getBoolean("block-non-arena-mythic-mob", false)) {
                    // Cant cancel event since we run this on later tick
                    event.getEntity().remove();
                }
            }
        }, 1L);
    }
}