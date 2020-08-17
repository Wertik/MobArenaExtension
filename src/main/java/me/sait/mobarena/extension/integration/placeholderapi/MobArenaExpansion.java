package me.sait.mobarena.extension.integration.placeholderapi;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sait.mobarena.extension.MobArenaExtensionPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MobArenaExpansion extends PlaceholderExpansion {

    private final MobArenaExtensionPlugin plugin;
    private final MobArena mobArena;

    public MobArenaExpansion(MobArena mobArena) {
        this.plugin = MobArenaExtensionPlugin.getInstance();
        this.mobArena = mobArena;
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return "mobarena";
    }

    @NotNull
    @Override
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @NotNull
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    //TODO recode
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String param) {

        if (param.equalsIgnoreCase("total_enabled")) {
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
                if (prefix == null) return "";
                return prefix;
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
}