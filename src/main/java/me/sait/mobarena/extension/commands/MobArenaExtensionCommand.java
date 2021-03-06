package me.sait.mobarena.extension.commands;

import me.sait.mobarena.extension.MobArenaExtensionPlugin;
import me.sait.mobarena.extension.extensions.Extension;
import me.sait.mobarena.extension.utils.CommonUtils;
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
            CommandUtils.sendHelp(sender, label);
            return true;
        }

        if (!sender.hasPermission("mobarenaextensions.control")) {
            sender.sendMessage(CommonUtils.color("&cYou don't have permissions to do this."));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "list":
                StringBuilder msg = new StringBuilder("&7Registered extensions: ");
                int n = 0;
                for (Extension extension : plugin.getExtensionManager().getExtensions()) {
                    msg.append(extension.isEnabled() ? "&a" : "&c").append(extension.getName());
                    if (n < plugin.getExtensionManager().getExtensions().size() - 1)
                        msg.append(", ");
                    n++;
                }
                sender.sendMessage(CommonUtils.color(msg.toString()));
                break;
            case "enable":
                if (args.length == 1) {
                    plugin.getExtensionManager().enableAll();
                    return true;
                }

                Extension extension = CommandUtils.getExtensionByName(sender, args[1]);

                if (extension == null) return true;

                if (extension.enable())
                    sender.sendMessage(CommonUtils.color("&7Enabled extension &f" + extension.getName()));
                else
                    sender.sendMessage(CommonUtils.color("&cCould not enable &f" + extension.getName()));
                break;
            case "disable":
                if (args.length == 1) {
                    plugin.getExtensionManager().disableAll();
                    return true;
                }

                extension = CommandUtils.getExtensionByName(sender, args[1]);

                if (extension == null) return true;

                extension.disable();
                sender.sendMessage(CommonUtils.color("&7Disabled extension &f" + extension.getName()));
                break;
            case "reload":
                if (args.length == 1) {
                    plugin.reload(sender);
                    return true;
                }

                extension = CommandUtils.getExtensionByName(sender, args[1]);

                if (extension == null) return true;

                extension.reload();
                sender.sendMessage(CommonUtils.color("&7Reloaded extension &f" + extension.getName()));
                break;
            default:
                CommandUtils.sendHelp(sender, label);
        }

        return false;
    }
}