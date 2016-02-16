package com.github.niwaniwa.we.core.util;

import com.github.niwaniwa.we.core.WhiteEggCore;

import java.lang.reflect.InvocationTargetException;

/**
 * 必要なJarFileの読み込み
 *
 * @author mmott
 */
public class Reflection {

    private static final String nmsPackage = "net.minecraft.server.";
    private static final String cbPackage = "org.bukkit.craftbukkit.";
    private static final String version = WhiteEggCore.version.getCraftBukkitVersion();

    private Reflection() {
    }

    public static Class<?> getNMSClass(String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(String.format("%s%s.%s", nmsPackage, version, className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Class<?> getCBClass(String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(String.format("%s%s.%s", cbPackage, version, className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Object createPacketInstance(String packetClassName, Class<?>[] clazz, Object... args) {
        try {
            Class<?> packetClass = Class.forName(String.format("%s%s.%s", nmsPackage, version, packetClassName));
            if(clazz == null || clazz.length == 0){ return packetClass.newInstance(); }
            return packetClass.getConstructor(clazz).newInstance(args);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
