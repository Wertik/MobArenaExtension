package me.sait.mobarena.extension.extensions.internal.placeholderapi;

import com.garbagemule.MobArena.framework.Arena;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sait.mobarena.extension.MobArenaExtensionPlugin;
import me.sait.mobarena.extension.log.LogHelper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class MobArenaExpansion extends PlaceholderExpansion {

    private final MobArenaExtensionPlugin plugin;

    public MobArenaExpansion(PlaceholderExtension extension) {
        this.plugin = extension.getExtensionPlugin();
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

    /*
     * %mobarena_arena_<arena>_prefix%
     * %mobarena_arena_<arena>_players_(alive/dead)%
     * %mobarena_arena_<arena>_mobs%
     * %mobarena_arena_<arena>_statistic_<stat>%
     * %mobarena_arena_<arena>_wave_(final)%
     * */

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {

        String[] args = params.split("_");

        if (args.length == 0)
            return "not_enough_args";

        if (args[0].equalsIgnoreCase("arena")) {
            Arena arena = plugin.getMobArena().getArenaMaster().getArenaWithName(args[1]);

            if (arena == null) return "invalid_arena";

            switch (args[2].toLowerCase()) {
                case "prefix":
                    return arena.getSettings().getString("prefix", "");
                case "wave":
                    if (args.length == 4)
                        if (args[3].equalsIgnoreCase("final"))
                            return arena.getWaveManager().getFinalWave() > 0 ? String.valueOf(arena.getWaveManager().getFinalWave()) : "∞";
                        else break;
                    return String.valueOf(arena.getWaveManager().getWaveNumber());
                case "mobs":
                    return String.valueOf(arena.getMonsterManager().getMonsters().size());
                case "statistic":
                    if (player == null) return "no_player";

                    if (args.length == 4)
                        return String.valueOf(arena.getArenaPlayer(player).getStats().getInt(args[3]));
                case "players":
                    if (args.length == 4)
                        if (args[3].equalsIgnoreCase("alive"))
                            return String.valueOf(countAlivePlayers(arena));
                        else if (args[3].equalsIgnoreCase("dead"))
                            return String.valueOf(arena.getPlayerCount() - countAlivePlayers(arena));
                    return String.valueOf(arena.getPlayerCount());
            }
        } else if (args[0].equals("player")) {
            if (player == null) return "no_player";

            //TODO Create new Extension to capture player statistics and use them here.
            /*switch (args[1].toLowerCase()) {

            }*/
        }
        return "invalid_params";
    }

    private int countAlivePlayers(Arena arena) {
        return arena.getAllPlayers().stream()
                .map(arena::getArenaPlayer)
                .filter(ap -> !ap.isDead())
                .collect(Collectors.toSet())
                .size();
    }

    @Override
    public boolean register() {
        LogHelper.debug("Registered Mob Arena expansion.");
        return super.register();
    }
}