package me.sait.mobarena.extension.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.sait.mobarena.extension.MobArenaExtensionPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CommonUtils {
    public static String color(String str) {
        return str != null ? ChatColor.translateAlternateColorCodes('&', str) : null;
    }

    public static EntityType getEntityType(String entityType) {
        try {
            return EntityType.valueOf(entityType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static String parsePlaceholderAPI(String str, Player player) {
        return MobArenaExtensionPlugin.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI") != null ?
                PlaceholderAPI.setPlaceholders(player, str) : str;
    }
}