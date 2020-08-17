package me.sait.mobarena.extension.extensions.macommands;

import com.garbagemule.MobArena.events.*;
import me.sait.mobarena.extension.extensions.Extension;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CommandsExtension extends Extension {

    private final Map<String, List<String>> commandActions = new HashMap<>();
    private final Map<String, Listener> commandListeners = new HashMap<>();

    {
        commandListeners.put("start", new Listener() {
            @EventHandler
            public void onStart(ArenaStartEvent event) {
                runCommands("start", str -> createParser(str, "arena", event.getArena().arenaName()).toString());
            }
        });

        commandListeners.put("end", new Listener() {
            @EventHandler
            public void onEnd(ArenaEndEvent event) {
                runCommands("end", str -> createParser(str, "arena", event.getArena().arenaName()).toString());
            }
        });

        commandListeners.put("complete", new Listener() {
            @EventHandler
            public void onComplete(ArenaCompleteEvent event) {
                runCommands("complete", str -> createParser(str, "arena", event.getArena().arenaName())
                        .parse("survivor_count", event.getSurvivors().size())
                        .toString());
            }
        });

        commandListeners.put("kill", new Listener() {
            @EventHandler
            public void onKill(ArenaKillEvent event) {
                runCommands("kill", str -> createParser(str, "arena", event.getArena().arenaName())
                        .parse("player", event.getPlayer().getName())
                        .toString());
            }
        });

        commandListeners.put("player-death", new Listener() {
            @EventHandler
            public void onDeath(ArenaPlayerDeathEvent event) {
                runCommands("player-death", str -> createParser(str, "arena", event.getArena().arenaName())
                        .parse("player", event.getPlayer().getName())
                        .toString());
            }
        });

        commandListeners.put("player-join", new Listener() {
            @EventHandler
            public void onJoin(ArenaPlayerJoinEvent event) {
                runCommands("player-join", str -> createParser(str, "arena", event.getArena().arenaName())
                        .parse("player", event.getPlayer().getName())
                        .toString());
            }
        });

        commandListeners.put("player-ready", new Listener() {
            @EventHandler
            public void onReady(ArenaPlayerReadyEvent event) {
                runCommands("player-ready", str -> createParser(str, "arena", event.getArena().arenaName())
                        .parse("player", event.getPlayer().getName())
                        .toString());
            }
        });

        commandListeners.put("player-leave", new Listener() {
            @EventHandler
            public void onLeave(ArenaPlayerLeaveEvent event) {
                runCommands("player-leave", str -> createParser(str, "arena", event.getArena().arenaName())
                        .parse("player", event.getPlayer().getName())
                        .toString());
            }
        });
    }

    @Override
    public String getName() {
        return "macommands";
    }

    @Override
    public String getPluginName() {
        return null;
    }

    @Override
    public boolean onEnable() {
        registerListeners();
        return true;
    }

    private void registerListeners() {
        for (Map.Entry<String, Listener> entry : commandListeners.entrySet()) {
            getExtensionPlugin().getServer().getPluginManager().registerEvents(entry.getValue(), getExtensionPlugin());
            commandActions.put(entry.getKey(), getSection().getStringList("commands." + entry.getKey()));
        }
    }

    private void loadCommands() {
        for (String action : commandListeners.keySet())
            commandActions.put(action, getSection().getStringList("commands." + action));
    }

    @Override
    public void onReload() {
        loadCommands();
    }

    @Override
    public void onDisable() {
    }

    public class StringParser {
        private String parsedString;

        public StringParser(String str) {
            this.parsedString = str;
        }

        public StringParser parse(String key, Object value) {
            parsedString = parsedString.replaceAll("(?i)%" + key + "%", value.toString());
            return this;
        }

        public String toString() {
            return parsedString;
        }
    }

    private StringParser createParser(String in, String key, Object value) {
        in = in.replaceAll("(?i)%" + key + "%", value.toString());
        return new StringParser(in);
    }

    public void runCommands(String action) {
        runCommands(action, null);
    }

    public void runCommands(String action, Function<String, String> parser) {
        for (String cmd : commandActions.get(action)) {
            if (parser != null)
                cmd = parser.apply(cmd);

            getExtensionPlugin().getServer().dispatchCommand(getExtensionPlugin().getServer().getConsoleSender(), cmd);
        }
    }
}