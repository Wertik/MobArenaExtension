package me.sait.mobarena.extension.commands;

import me.sait.mobarena.extension.MobArenaExtensionPlugin;
import me.sait.mobarena.extension.extension.Extension;
import org.bukkit.command.CommandSender;

public class CommandUtils {
    public static Extension getExtensionByName(CommandSender sender, String extensionName) {

        Extension extension = MobArenaExtensionPlugin.getInstance().getExtensionManager().getExtension(extensionName);

        if (extension == null) {
            //TODO err msg
            return null;
        }

        return extension;
    }

    public static void sendHelp(CommandSender sender, String label) {
        sender.sendMessage("&8&m    &c Mob Arena Extensions &8&m    " +
                "\n&c/%label% &8- &7Displays this." +
                "\n&c/%label% reload (extension) &8- &7Reload an extension, or all." +
                "\n&c/%label% enable (extension) &8- &7Enable an extension, or all." +
                "\n&c/%label% disable (extension) &8- &7Disable an extension, or all."
                        .replace("%label%", label));
    }
}