package me.sait.mobarena.extension.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

import java.util.Collection;

public class CommonUtils {
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

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
}