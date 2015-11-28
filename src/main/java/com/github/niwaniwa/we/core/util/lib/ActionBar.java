package com.github.niwaniwa.we.core.util.lib;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.niwaniwa.we.core.util.Extension;

import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class ActionBar extends Extension {

	private String str;

	public ActionBar(String str){
		this.str = str;
	}

	public ActionBar(String str, ChatColor color){
		this("§" + color.getChar() + str);
	}

	public ActionBar(){
		this("");
	}

	public void setMessage(String str){
		this.str = str;
	}

	@Override
	public void send(Player player) {
		PacketPlayOutChat packet = new PacketPlayOutChat(build(str), (byte) 2);
		cast(player).getHandle().playerConnection.sendPacket(packet);
	}

}
