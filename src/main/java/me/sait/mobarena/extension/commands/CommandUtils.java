package me.sait.mobarena.extension.commands;

import me.sait.mobarena.extension.MobArenaExtensionPlugin;
import me.sait.mobarena.extension.extension.Extension;
import me.sait.mobarena.extension.utils.CommonUtils;
import org.bukkit.command.CommandSender;

public class CommandUtils {
    public static Extension getExtensionByName(CommandSender sender, String extensionName) {

        Extension extension = MobArenaExtensionPlugin.getInstance().getExtensionManager().getExtension(extensionName);

        if (extension == null) {
            sender.sendMessage(CommonUtils.color("&cExtension name &f" + extensionName + "&c is not valid."));
            return null;
        }

        return extension;
    }

    public static void sendHelp(CommandSender sender, String label) {
        sender.sendMessage(CommonUtils.color(("&8&m    &c Mob Arena Extensions &8&m    " +
                "\n&c/%label% &8- &7Displays this." +
                "\n&c/%label% list &8- &7List installed extensions." +
                "\n&c/%label% reload (extension) &8- &7Reload an extension, or all." +
                "\n&c/%label% enable (extension) &8- &7Enable an extension, or all." +
                "\n&c/%label% disable (extension) &8- &7Disable an extension, or all.")
                .replace("%label%", label)));
    }
}
