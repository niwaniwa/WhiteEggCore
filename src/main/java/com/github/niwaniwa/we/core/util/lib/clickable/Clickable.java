package com.github.niwaniwa.we.core.util.lib.clickable;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.api.WhiteEggAPI;
import com.github.niwaniwa.we.core.util.Extension;
import com.github.niwaniwa.we.core.util.Reflection;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Clickable extends Extension {

    private JsonObject json = new JsonObject();

    /**
     * Constructor
     *
     * @param text    表示する文字列
     * @param color   文字列の装飾
     * @param formats フォーマット
     */
    public Clickable(String text, ChatColor color, List<ChatFormat> formats) {
        json.addProperty("text", text);
        if (color != null) {
            json.addProperty("color", color.toString().toLowerCase());
        }
        if (formats != null) {
            formats.forEach(f -> json.addProperty(f.getFormat(), true));
        }
    }

    public Clickable(String text) {
        this(text, ChatColor.WHITE, Lists.newArrayList());
    }

    public void setColor(ChatColor color) {
        json.addProperty("color", color.toString().toLowerCase());
    }

    public void setFormat(List<ChatFormat> formats) {
        formats.forEach(f -> json.addProperty(f.getFormat(), true));
    }

    /**
     * Eventなどを追加します
     *
     * @param extraInstance ChatExtra
     */
    @Deprecated
    public void addExtra(ChatExtra extraInstance) {
        JsonArray extra = new JsonArray();
        extra.add(extraInstance.toJson());
        json.add("extra", extra);
    }

    @Override
    public void send(Player player) {
        if(WhiteEggCore.getInstance().isEnabled()){
            WhiteEggAPI.getPlayer(player).sendMessage("...現在調整中...");
            return;
        }

        Class<?> chatSerializer = Reflection.getNMSClass("IChatBaseComponent$ChatSerializer");
        Class<?> iChatBaseComponent = Reflection.getNMSClass("IChatBaseComponent");
        try {
            Object packet = Reflection.createPacketInstance("PacketPlayOutChat", new Class[]{iChatBaseComponent}, chatSerializer.getDeclaredMethod("a", String.class).invoke(null, json.toString()));
            this.sendPacket(player, packet);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            e.printStackTrace();
        }
    }

}
