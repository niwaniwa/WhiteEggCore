package com.github.niwaniwa.we.core.util.lib.clickable;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.util.Extension;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class Clickable extends Extension {

	private JsonObject json;

	/**
	 * Constructor
	 * @param text 表示する文字列
	 * @param color 文字列の装飾
	 * @param formats フォーマット
	 */
	public Clickable(String text, ChatColor color, List<ChatFormat> formats) {
		json = new JsonObject();
		json.addProperty("text", text);
		if (color != null) {
			json.addProperty("color", color.toString().toLowerCase());
		}
		if (formats != null) { formats.forEach(f -> json.addProperty(f.getFormat(), true)); }
	}

	/**
	 * Eventなどを追加します
	 * @param extraObject ChatExtra
	 */
	public void addExtra(ChatExtra extraInstance) {
		if (json.get("extra") == null) { return; }
		JsonArray extra = json.getAsJsonArray("extra");
		extra.add(extraInstance.toJson());
		json.add("extra", extra);
	}

	@Override
	public void send(Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(json.toString())));
	}

}
