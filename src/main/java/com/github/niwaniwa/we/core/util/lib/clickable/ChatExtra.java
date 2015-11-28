package com.github.niwaniwa.we.core.util.clickable;

import java.util.List;

import org.bukkit.ChatColor;

import com.google.gson.JsonObject;

public class ChatExtra {

	private JsonObject json;

	public ChatExtra(String text, ChatColor color, List<ChatFormat> formats) {
		json = new JsonObject();
		json.addProperty("text", text);
		json.addProperty("color", color.toString().toLowerCase());
		formats.forEach(f -> json.addProperty(f.getFormat(), true));
	}

	public void setClickEvent(ClickEventType action, String value) {
		JsonObject clickEvent = new JsonObject();
		clickEvent.addProperty("action", action.getType());
		clickEvent.addProperty("value", value);
		json.add("clickEvent", clickEvent);
	}

	public void setHoverEvent(HoverEventType action, String value) {
		JsonObject hoverEvent = new JsonObject();
		hoverEvent.addProperty("action", action.getType());
		hoverEvent.addProperty("value", value);
		json.add("hoverEvent", hoverEvent);
	}

	public JsonObject toJSON() {
		return json;
	}

}
