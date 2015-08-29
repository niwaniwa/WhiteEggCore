package com.github.niwaniwa.we.core.util.clickable;

import java.util.List;

import org.bukkit.ChatColor;

import net.sf.json.JSONObject;

public class ChatExtra {

	private JSONObject json;

	public ChatExtra(String text, ChatColor color, List<ChatFormat> formats) {
		json = new JSONObject();
		json.put("text", text);
		json.put("color", color.toString().toLowerCase());
		for (ChatFormat format : formats) {
			json.put(format.getFormat(), true);
		}
	}

	public void setClickEvent(ClickEventType action, String value) {
		JSONObject clickEvent = new JSONObject();
		clickEvent.put("action", action.getType());
		clickEvent.put("value", value);
		json.put("clickEvent", clickEvent);
	}

	public void setHoverEvent(HoverEventType action, String value) {
		JSONObject hoverEvent = new JSONObject();
		hoverEvent.put("action", action.getType());
		hoverEvent.put("value", value);
		json.put("hoverEvent", hoverEvent);
	}

	public JSONObject toJSON() {
		return json;
	}

}
