package com.github.niwaniwa.we.core.util.lib.clickable;

import java.util.List;

import org.bukkit.ChatColor;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

public class ChatExtra {

	private JsonObject json = new JsonObject();;

	/**
	 * Constructor
	 * @param text 表示する文字列
	 * @param color 文字列の色
	 * @param formats 使用するフォーマット
	 */
	public ChatExtra(String text, ChatColor color, List<ChatFormat> formats) {
		json.addProperty("text", text);
		if(color != null){ json.addProperty("color", color.toString().toLowerCase()); }
		else { json.addProperty("color", ChatColor.WHITE.toString().toLowerCase()); }
		formats.forEach(f -> json.addProperty(f.getFormat(), true));
	}

	public ChatExtra(String text){ this(text, ChatColor.WHITE, Lists.newArrayList()); }

	public void setColor(ChatColor color){ json.addProperty("color", color.toString().toLowerCase()); }

	public void setFormat(List<ChatFormat> formats){ formats.forEach(f -> json.addProperty(f.getFormat(), true)); }

	/**
	 * ClickEventの設定をします
	 * @param action クリック時にどのような挙動かを設定します
	 * @param value 対応する文字列
	 */
	public void setClickEvent(ClickEventType action, String value) {
		JsonObject clickEvent = new JsonObject();
		clickEvent.addProperty("action", action.getType());
		clickEvent.addProperty("value", value);
		json.add("clickEvent", clickEvent);
	}

	/**
	 * HoverEventの設定をします
	 * @param action マウスがテキスト上に載った際の挙動
	 * @param value 対応する文字列
	 */
	public void setHoverEvent(HoverEventType action, String value) {
		JsonObject hoverEvent = new JsonObject();
		hoverEvent.addProperty("action", action.getType());
		hoverEvent.addProperty("value", value);
		json.add("hoverEvent", hoverEvent);
	}

	/**
	 * 設定をJsonObjectとして返します
	 * @return JsonObject json
	 */
	public JsonObject toJson() {
		return json;
	}

}
