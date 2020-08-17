package me.sait.mobarena.extension.utils;

import java.util.Collection;

public class CommonUtils {
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}