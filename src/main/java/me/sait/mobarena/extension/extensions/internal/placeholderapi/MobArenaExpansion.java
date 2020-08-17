package me.sait.mobarena.extension.extensions.internal.placeholderapi;

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

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {

        if (params.equalsIgnoreCase("total_enabled")) {
            return String.valueOf(mobArena.getArenaMaster().getEnabledArenas().size());
        }

        if (player == null) return "";

        Arena arena = mobArena.getArenaMaster().getArenaWithPlayer(player);
        if (arena == null) {
            arena = mobArena.getArenaMaster().getArenaWithSpectator(player);

            if (arena == null) return "no_arena";
        }

        String[] args = params.split("_");

        if (args.length == 0)
            return "not_enough_args";

        if (args[0].equalsIgnoreCase("arena")) {
            if (args.length == 1) {
                return arena.arenaName();
            }

            switch (args[1].toLowerCase()) {
                case "prefix":
                    return arena.getSettings().getString("prefix", "");
                case "wave":
                    if (args.length == 3)
                        if (args[2].equalsIgnoreCase("final"))
                            return arena.getWaveManager().getFinalWave() > 0 ? String.valueOf(arena.getWaveManager().getFinalWave()) : "∞";
                        else break;
                    return String.valueOf(arena.getWaveManager().getWaveNumber());
                case "mobs":
                    return String.valueOf(arena.getMonsterManager().getMonsters().size());
                case "statistic":
                    if (args.length == 3)
                        return String.valueOf(arena.getArenaPlayer(player).getStats().getInt(args[2]));
                case "players":
                    if (args.length == 3 && args[2].equalsIgnoreCase("alive"))
                        return String.valueOf(arena.getPlayerCount());
            }
        } /*else if (args[0].equals("player")) {

        }*/
        return "invalid_params";
    }
}