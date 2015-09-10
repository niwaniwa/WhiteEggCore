package com.github.niwaniwa.we.core.player;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;

public class WhiteConsoleSender implements WhiteCommandSender {

	boolean b = false;

	public WhiteConsoleSender(boolean b) {
		this.b = b;
	}

	@Override
	public void sendMessage(String message) {
		sendMessage(message, true);
	}

	@Override
	public void sendMessage(String message, boolean replaceColorCode) {
		Logger.getGlobal().info(ChatColor.translateAlternateColorCodes('&', message));
	}

	@Override
	public boolean hasPermission(String permission) {
		return b;
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return b;
	}

}
