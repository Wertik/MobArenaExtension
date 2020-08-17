package me.sait.mobarena.extension.integration.placeholderapi;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sait.mobarena.extension.MobArenaExtension;
import me.sait.mobarena.extension.api.Integration;
import me.sait.mobarena.extension.config.Constants;
import me.sait.mobarena.extension.integration.placeholderapi.events.MAPlaceholderEvent;
import me.sait.mobarena.extension.log.LogHelper;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPISupport extends PlaceholderExpansion implements Integration {

    public static final String PLUGIN_NAME = "PlaceholderAPI";

    private static final String identifier = "mobarena";

    private final MobArenaExtension extension;
    private final MobArena mobArena;

    public PlaceholderAPISupport(MobArenaExtension extension, MobArena mobArena) {
        this.extension = extension;
        this.mobArena = mobArena;
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return identifier;
    }

    @NotNull
    @Override
    public String getAuthor() {
        return String.join(", ", extension.getDescription().getAuthors());
    }

    @NotNull
    @Override
    public String getVersion() {
        return extension.getDescription().getVersion();
    }

    @Override
    public void onEnable() {
        register();
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onDisable() {
        unregister();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String param) {

        MAPlaceholderEvent event = new MAPlaceholderEvent(param);
        Bukkit.getPluginManager().callEvent(event);

        if (event.getResult() != null) {
            if (StringUtils.isBlank(event.getResult())) {
                LogHelper.debug("Some plugin override this placeholder with an empty value: " + param);
            }
            return event.getResult();
        }

        if (param.equalsIgnoreCase("prefix")) {
            return getGlobalPrefix();
        } else if (param.equalsIgnoreCase("total_enabled")) {
            return String.valueOf(mobArena.getArenaMaster().getEnabledArenas().size());
        }

        //player specific
        if (player == null) return "";

        Arena arena = mobArena.getArenaMaster().getArenaWithPlayer(player);
        if (arena == null) {
            mobArena.getArenaMaster().getArenaWithSpectator(player);
        }

        if (param.toLowerCase().startsWith("arena")) {
            if (arena == null) return "";

            if (param.equalsIgnoreCase("arena") || param.equalsIgnoreCase("arena_name")) {
                return arena.getSettings().getName();

            } else if (param.equalsIgnoreCase("arena_prefix")) {
                String prefix = arena.getSettings().getString("prefix", "");
                if (prefix.isEmpty()) {
                    return getGlobalPrefix();
                } else {
                    return prefix;
                }

            } else if (param.equalsIgnoreCase("arena_wave")) {
                return String.valueOf(arena.getWaveManager().getWaveNumber());

            } else if (param.equalsIgnoreCase("arena_final_wave")) {
                if (arena.getWaveManager().getFinalWave() > 0) {
                    return String.valueOf(arena.getWaveManager().getFinalWave());
                } else {
                    return "∞";
                }

            } else if (param.equalsIgnoreCase("arena_mobs")) {
                return String.valueOf(arena.getMonsterManager().getMonsters().size());

                //TODO those stats provided by core with keys were hard coded with name, might broken in the future
            } else if (param.equalsIgnoreCase("arena_killed")) {
                return String.valueOf(arena.getArenaPlayer(player).getStats().getInt("kills"));

            } else if (param.equalsIgnoreCase("arena_damage_dealt")) {
                return String.valueOf(arena.getArenaPlayer(player).getStats().getInt("dmgDone"));

            } else if (param.equalsIgnoreCase("arena_damage_received")) {
                return String.valueOf(arena.getArenaPlayer(player).getStats().getInt("dmgTaken"));
            }
        } else if (param.toLowerCase().startsWith("player")) {
            //TOD figure out what we want here
        }

        return null;
    }

    private String getGlobalPrefix() {
        String prefix = mobArena.getConfig().getString("global-settings.prefix", "");

        if (prefix.isEmpty()) {
            prefix = ChatColor.GREEN + "[" + Constants.MOB_ARENA_PLUGIN_NAME + "] ";
        }
        return prefix;
    }
}
