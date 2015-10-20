package com.github.niwaniwa.we.core.util.clickable;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.util.Extension;

import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Clickable extends Extension {

	private JSONObject json;

	public Clickable(String text, ChatColor color, List<ChatFormat> formats) {
		json = new JSONObject();
		json.put("text", text);
		if (color != null) {
			json.put("color", color.toString().toLowerCase());
		}
		if (formats != null) {
			for (ChatFormat f : formats) {
				json.put(f.getFormat(), true);
			}
		}
	}

	public void addExtra(ChatExtra extraObject) {
		if (!json.containsKey("extra")) {
			json.put("extra", new JSONArray());
		}
		JSONArray extra = (JSONArray) json.get("extra");
		extra.add(extraObject.toJSON());
		json.put("extra", extra);
	}

	@Override
	public void send(Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(json.toString())));
	}

}
