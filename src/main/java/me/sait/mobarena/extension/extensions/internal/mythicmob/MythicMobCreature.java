package me.sait.mobarena.extension.extensions.internal.mythicmob;

import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.waves.MACreature;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import me.sait.mobarena.extension.log.LogHelper;
import me.sait.mobarena.extension.utils.CommonUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class MythicMobCreature extends MACreature {

    private final MythicMobsExtension extension;
    private final MythicMob mythicMob;

    private final boolean livingEntity;

    public MythicMobCreature(MythicMobsExtension extension, MythicMob mythicMob) {
        super(mythicMob.getInternalName().toLowerCase().replaceAll("[-_.]", ""),
                CommonUtils.getEntityType(mythicMob.getEntityType()));

        this.extension = extension;
        this.mythicMob = mythicMob;

        EntityType entityType = CommonUtils.getEntityType(mythicMob.getEntityType());

        this.livingEntity = entityType != null && entityType.isAlive();

        if (!livingEntity)
            LogHelper.warn(mythicMob.getInternalName() + " is not a living entity, currently not compatible with Mob Arena.");
    }

    @Override
    public LivingEntity spawn(Arena arena, World world, Location location) {
        try {
            Entity mythicEntity = MythicMobs.inst().getAPIHelper().spawnMythicMob(mythicMob, location, 0);

            if (!this.livingEntity) {
                LogHelper.error(mythicMob.getInternalName() + " is not a living entity, cant spawn in Mob Arena");
                mythicEntity.remove();
                return null;
            }

            extension.spawnMythicMob(arena, mythicEntity);
            LivingEntity livingEntity = ((LivingEntity) mythicEntity);

            // Temp fix for MA core reset mm hp due to implementation of health-multiplier
            double multiplier = arena.getWaveManager().getCurrent().getHealthMultiplier();
            double maxHp = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

            extension.runTask(() -> {
                double health = maxHp * multiplier;

                livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
                livingEntity.setHealth(Math.max(1D, health));
            }, 1);

            return livingEntity;
        } catch (InvalidMobTypeException e) {
            return null;
        }
    }
}
