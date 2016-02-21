package com.github.niwaniwa.we.core.util;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Extension {

    /**
     * プレイヤーにパケットなどを送信します
     *
     * @param player 対象プレイヤー
     */
    public abstract void send(Player player);

    protected Object build(String source) {
        Class<?> chatSerializer = Reflection.getNMSClass("IChatBaseComponent$ChatSerializer");
        try {
            JsonObject json = new JsonObject();
            json.addProperty("text", source);
            return chatSerializer.getDeclaredMethod("a", String.class).invoke(null, json.toString());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 指定した権限を所持しているプレイヤーのみに送信します
     *
     * @param prm 権限
     * @param t   Extension
     */
    public static void permissionBroadcast(String prm, Extension t) {
        Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission(prm)).forEach(p -> t.send(p));
    }

    /**
     * 指定したワールドにいるプレイヤーに送信します
     *
     * @param world world
     * @param t     Extension
     */
    public static void worldBroadcast(World world, Extension t) {
        world.getPlayers().forEach(player -> t.send(player));
    }

    /**
     * すべてのプレイヤーに送信します
     *
     * @param t Extension
     */
    public static void serverBroadcast(Extension t) {
        Bukkit.getOnlinePlayers().forEach(player -> t.send(player));
    }

    public void sendPacket(Player player, Object packet) {
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
            Method sendPacketMethod = playerConnection.getClass().getMethod("sendPacket", Reflection.getNMSClass("Packet"));
            sendPacketMethod.invoke(playerConnection, packet);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
