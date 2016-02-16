package com.github.niwaniwa.we.core.util.lib;

import com.github.niwaniwa.we.core.util.Extension;
import com.github.niwaniwa.we.core.util.Reflection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class Tab  extends Extension {

	String header;
	String footer;

	public Tab(String header, String footer,
			ChatColor headerColor, ChatColor footerColor){
			this.header = "§" + headerColor.getChar() + header;
			this.footer = "§" + footerColor.getChar() + footer;
	}

	public Tab(String header, ChatColor headerColor,
			String footer){
			this(header,footer,headerColor,ChatColor.WHITE);
	}

	public Tab(String header, String footer,
			ChatColor footerColor){
			this(header,footer,ChatColor.WHITE,footerColor);
	}

	public Tab(String header, ChatColor headerColor){
		this(header,"",headerColor,ChatColor.WHITE);
	}

	public Tab(String header, String footer){
			this(header,footer,ChatColor.WHITE,ChatColor.WHITE);
	}

	public Tab(String header){
		this(header,"",ChatColor.WHITE,ChatColor.WHITE);
}

	public void setHeader(String header){
		this.header = header;
	}

	public void setFooter(String footer){
		this.footer = footer;
	}

	public void setHeaderColor(ChatColor color){
		this.header = "§" + color.getChar() + header;
	}

	public void setFooterColor(ChatColor color){
		this.footer = "§" + color.getChar() + footer;
	}

	@Override
	public void send(Player player) {

		Object packet = Reflection.createPacketInstance("PacketPlayOutPlayerListHeaderFooter", null);

		try {

			Field h = packet.getClass().getDeclaredField("a");
			Field f = packet.getClass().getDeclaredField("b");

			h.setAccessible(true);
			h.set(packet, build(header));
			h.setAccessible(!h.isAccessible());

			f.setAccessible(true);
			f.set(packet, build(footer));
			f.setAccessible(!f.isAccessible());

			sendPacket(player, packet);

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}

}
