package me.sait.mobarena.extension.commands;

import me.sait.mobarena.extension.MobArenaExtensionPlugin;
import me.sait.mobarena.extension.extension.Extension;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MobArenaExtensionCommand implements CommandExecutor {

    private final MobArenaExtensionPlugin plugin;

    public MobArenaExtensionCommand(MobArenaExtensionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            //TODO help
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "enable":
                if (args.length == 1) {
                    plugin.getExtensionManager().enable();
                    return true;
                }

                Extension extension = CommandUtils.getExtensionByName(sender, args[1]);

                if (extension == null) return true;

                plugin.getExtensionManager().enableExtension(extension);
                sender.sendMessage("&7Enabled extension &f" + extension.getName());
                break;
            case "disable":
                if (args.length == 1) {
                    plugin.getExtensionManager().disable();
                    return true;
                }

                extension = CommandUtils.getExtensionByName(sender, args[1]);

                if (extension == null) return true;

                plugin.getExtensionManager().disableExtension(extension);
                sender.sendMessage("&7Disabled extension &f" + extension.getName());
                break;
            case "reload":
                if (args.length == 1) {
                    plugin.reload(sender);
                    return true;
                }

                extension = CommandUtils.getExtensionByName(sender, args[1]);

                if (extension == null) return true;

                plugin.getExtensionManager().reloadExtension(extension);
                sender.sendMessage("&7Reloaded extension &f" + extension.getName());
                break;
        }

        return false;
    }
}