package com.github.niwaniwa.we.core.util.clickable;

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

	public Clickable(String text, ChatColor color, List<ChatFormat> formats) {
		json = new JsonObject();
		json.addProperty("text", text);
		if (color != null) {
			json.addProperty("color", color.toString().toLowerCase());
		}
		if (formats != null) { formats.forEach(f -> json.addProperty(f.getFormat(), true)); }
	}

	public void addExtra(ChatExtra extraObject) {
		if (json.get("extra") == null) {
			json.add("extra", new JsonArray());
		}
		JsonArray extra = json.getAsJsonArray("extra");
		extra.add(extraObject.toJSON());
		json.add("extra", extra);
	}

	@Override
	public void send(Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(json.toString())));
	}

}
